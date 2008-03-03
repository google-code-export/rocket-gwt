/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.widget.rebind.imagefactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.util.MethodComparator;
import rocket.generator.rebind.visitor.VirtualMethodVisitor;
import rocket.util.client.Checker;
import rocket.util.client.Tester;
import rocket.util.server.Base64Encoder;
import rocket.util.server.InputOutput;

/**
 * This factory generates ImageFactory implementations and any accompanying server files. 
 * @author Miroslav Pokorny
 */
abstract public class ImageFactoryGenerator extends Generator {

	//@Override
	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final Set methods = this.findMethods(type);
		final NewConcreteType generatedType = this.createType( type, newTypeName );		
		final Set prefetchUrls = this.implementMethods( generatedType, methods  );
		this.overloadGetPreloadUrls(generatedType, prefetchUrls);
		return generatedType;
	}

	/**
	 * The suffix for the generated class will include the user agent.
	 */
	//@Override
	protected String getGeneratedTypeNameSuffix() {
		final String userAgent = this.getGeneratorContext().getProperty(Constants.USER_AGENT);
		final MessageFormat message = new MessageFormat(Constants.SUFFIX);
		return message.format(Constants.SUFFIX, new String[]{ userAgent });
	}

	/**
	 * Finds and records all methods belonging to the given type.
	 * @param type
	 * @return
	 */
	protected Set findMethods(final Type type) {
		Checker.notNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Finding interface methods which will be implemented");

		final Set methods = new TreeSet( MethodComparator.INSTANCE ); // sort methods in alphabetical order

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
			protected boolean visit(final Method method) {
				context.debug(method.getName());
				ImageFactoryGenerator.this.checkMethod(method);
				methods.add(method);
				return false; // continue visiting other methods.
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		visitor.start( type );
		
		context.unbranch();
		context.debug("Found " + methods.size() + " methods.");
		return methods;
	}

	/**
	 * Validates that the given method doesnt have any parameters and returns Image.
	 * If any of the these two tests fail an exception is thrown.
	 * @param method
	 */
	protected void checkMethod(final Method method) {
		Checker.notNull("parameter:method", method);

		// must have no parameters
		final List parameters = method.getParameters();
		if (false == parameters.isEmpty()) {
			throwInterfaceMethodHasParameters(method);
		}

		// must return Image.
		final Type type = method.getReturnType();
		final Type image = this.getImage();
		if (type != image) {
			this.throwWrongReturnType(method);
		}
	}

	protected void throwInterfaceMethodHasParameters(final Method method) {
		this.throwException(new ImageFactoryGeneratorException("Must must not have any parameters, method: \"" + this.toString( method) + "\"."));
	}

	protected void throwWrongReturnType(final Method method) {
		this.throwException(new ImageFactoryGeneratorException("Interface methods must return " + Constants.IMAGE_TYPE + " and not "
				+ method.getReturnType() + ", method: " + method));
	}

	protected Type getImage() {
		return this.getGeneratorContext().getType(Constants.IMAGE_TYPE);
	}

	/**
	 * Creates a skeleton base class ready to accepte implemented methods.
	 * @param type The original interface to be implemented
	 * @param newTypeName The name for the new type
	 * @return A new type
	 */
	protected NewConcreteType createType(final Type type, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info( "Creating new type");

		final NewConcreteType newType = context.newConcreteType( newTypeName );
		newType.setAbstract( false );
		newType.setFinal( true );
		
		final Type superType = this.getImageFactoryImpl();
		newType.setSuperType( superType );
		
		newType.addInterface( type );
		
		newType.setVisibility( Visibility.PUBLIC );
		
		context.debug( "Name: " + newTypeName );
		context.debug( "SuperType: " + superType.getName() );
		context.debug( "Implements: " + type.getName() );
		context.debug( "public");
		context.debug( "final");
		
		context.unbranch();
		
		return newType;
	}
	
	protected Type getImageFactoryImpl(){
		return this.getGeneratorContext().getType( Constants.IMAGE_FACTORY_IMPL_TYPE );
	}
	
	/**
	 * Implements all methods taken from the original interface in the given concrete type.
	 * @param newType
	 * @param methods
	 * @return A set containing the urls to prefetch
	 */
	protected Set implementMethods( final NewConcreteType newType, final Set methods ){
		Checker.notNull("parameter:methods", methods );
		
		final Set urls = new TreeSet();
		
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Implementing methods.");
		
		final Iterator iterator = methods.iterator();
		while( iterator.hasNext() ){
			final Method method = (Method) iterator.next();
			final String prefetch = this.implementMethod( newType, method );
			if( prefetch != null ){
				urls.add( prefetch );
			}
		}
			
		context.unbranch();	
		
		return urls;
	}
	
	/**
	 * Implements a single method, using the appropriate url depending on the supported characteristics of the browser.
	 * 
	 * If a data url may be used then the contents are encoded and this url is used.
	 * For cases where a data url is not appropriate the file is copied to the public output directory using a filename that is computed from a hash of its contents.
	 * 
	 * @param newType The type being generated
	 * @param method The method being processed.
	 */
	protected String implementMethod( final NewConcreteType newType, final Method method ){
		Checker.notNull("parameter:method", method );
		
		String prefetchUrl = null;
		
		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info( method.getName() );
		
		// get the filename
		final String resourceName = this.getImageName(method);
		context.debug( "Image: " + resourceName );
		
		// get the stream.
		final InputStream inputStream = this.getImage(newType, resourceName);
		
		// fetch the image
		final Image image = this.createImage( inputStream, resourceName );
		
		boolean usingDataUrl = false;
		String url = null;
		while( true ){
			final boolean forceServer = this.shouldAlwaysRequestImageFromServer( method );
			context.debug( "Always make server requests: " + ( forceServer ? "yes" : "no"));
			if( forceServer ){
				break;
			}
			
			// are data urls arent supported ?
			final int maximumDataUrlLength = this.getMaximumDataUrlLength();
			if( 0 == maximumDataUrlLength ){
				context.debug( "Data urls are not supported.");
				break;
			}	
			
			// check if data url isnt too long.
			final String dataUrl = image.getDataUrl();						
			context.debug( "Data url length: " + dataUrl.length() );
			context.debug( "Maximum browser supported data url length: " + maximumDataUrlLength );
			
			if( dataUrl.length() > maximumDataUrlLength ){
				context.debug( "Data url for image is too large, requests will fetch image from server.");
				break;
			}
			
			// data url length is ok.
			url = dataUrl;
			
			// be nice and log the first 64 chars of the data url
			String shortDataUrl = dataUrl;
			if( shortDataUrl.length() > 64 ){
				shortDataUrl = shortDataUrl.substring( 0, 61 ) + "...";
			}
			
			context.debug( "Data url: " + shortDataUrl );
			usingDataUrl = true;
			break;
		}
		
		// if a data url wasnt used copy the file and set the url to the server file name.
		if( false == usingDataUrl ){
			// data urls are supported, write the file 
			final String suffix = Constants.IMAGE_RESOURCE_SUFFIX + image.getFileExtension();
			url = context.createResource( image.getContents(), suffix );
			
			context.debug( "Module relative server url: \"" + url + "\".");
			
			final boolean prefetch = this.shouldPrefetchServerImage( method );
			if( prefetch ){
				prefetchUrl = url;
			}
		}
		
		// time to write the contents of the method.
		final NewMethod newMethod = method.copy( newType );
		newMethod.setAbstract( false );
		newMethod.setFinal( true );
		newMethod.setNative( false );
		
		final CreateImageTemplatedFile body = new CreateImageTemplatedFile();
		body.setUrl(url);
		newMethod.setBody(body);
		
		context.unbranch();		
		
		return prefetchUrl;
	}
	
	/**
	 * Fetches the resource name for the image belonging to this method. 
	 * @param method
	 * @return
	 */
	protected String getImageName( final Method method ){
		final List filenames = method.getMetadataValues( Constants.IMAGE_FILE );
		if( null == filenames || filenames.size() != 1 ){
			throwExpectedOneImageFilename( method );
		}
		
		return (String) filenames.get( 0 );
	}
	
	protected void throwExpectedOneImageFilename( final Method method ){
		throwException( new ImageFactoryGeneratorException( "Expected to find one \"" + Constants.IMAGE_FILE + "\" annotation on method: \"" + this.toString( method) + "\"."));
	}
	
	/**
	 * Retrieves an inputStream which may be used to read the contents of an image file. If the resource cannot be located an exception is thrown.
	 * @param resourceName
	 * @return An inputStream which may be used to read the image in its entirety
	 */
	protected InputStream getImage( final Type type, final String resourceName ){
		final String fullResourceName = this.getResourceName( type.getPackage(), resourceName );		
		final InputStream inputStream = this.getResource( fullResourceName );
		if( null == inputStream ){
			throwImageNotFound( fullResourceName );
		}
		return inputStream;
	}
	
	protected void throwImageNotFound( final String resourceName ){
		throwException( new ImageFactoryGeneratorException( "Unable to locate a image file called \"" + resourceName + "\"."));
	}
	
	/**
	 * Attempts to load and creates an Image to hold the image.
	 * @param inputStream
	 * @param resourceName
	 * @return A new Image
	 */
	protected Image createImage( final InputStream inputStream, final String resourceName ){
		final Image image = new Image();
		
		try{
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buffer = new byte[ 4096 ];
			while( true ){
				final int readCount = inputStream.read( buffer );
				if( -1 == readCount ){
					break;
				}
				output.write( buffer, 0, readCount );
				output.flush();
				
				image.setContents( output.toByteArray() );
			}
		} catch ( final IOException io ){
			throwException( new ImageFactoryGeneratorException( "Unable to load image from \"" + resourceName + "\", cause: " + io.getMessage() ));
		}
		
		image.setResourceName(resourceName);
		return image;
	}
	
	/**
	 * Instances of this class represent an image resource loaded from the file system.
	 */
	static class Image{
		/**
		 * The byte contents of the image.
		 */
		byte[] contents;
		
		public byte[] getContents(){
			Checker.notNull("field:contents", contents );
			return contents;
		}
		
		public void setContents( final byte[] contents ){
			Checker.notNull("parameter:contents", contents );
			this.contents = contents;
			
			this.setImage( this.loadImage(contents));
		}
		
		protected java.awt.Image loadImage( final byte[] contents ){
			Checker.notNull("parameter:contents", contents );
			
			try{
				return ImageIO.read(new ByteArrayInputStream( contents ));
				
			} catch ( final IOException io ){
				InputOutput.throwIOException(io);
				return null;// never happens
			}
		}
		
		public String getDataUrl(){
			final String mimeType = this.getMimeType();
			final String encoded = this.base64Encode( this.getContents() );
			return "data:" + mimeType + ";base64," + encoded;
		}
		
		protected String base64Encode( final byte[] bytes ){
			final Base64Encoder encoder = new Base64Encoder();
			return encoder.encode( bytes );
		}
		
		public int getWidth(){
			return this.getImage().getWidth( null );
		}
		
		public int getHeight(){
			return this.getImage().getHeight( null );
		}
		
		// FIXME Must find a better way to determine the mime type of an image.
		public String getMimeType(){			
			String mimeType = null;
			
			while( true ){
				final String fileExtension = this.getFileExtension();
				if( fileExtension.equals( "jpg" ) ){
					mimeType = "image/jpg";
					break;
				}
				if( fileExtension.equals( "jpeg" ) ){
					mimeType = "image/jpeg";
					break;
				}
				if( fileExtension.equals( "gif" )){
					mimeType = "image/gif";
					break;
				}
				if( fileExtension.equals( "png" )){
					mimeType = "image/png";
					break;
				}
				
				
				throw new IllegalStateException( "Unable to determine the mimetype from the file \"" + resourceName + "\".");				
			}
			
			return mimeType;
		}
		
		private java.awt.Image image;
		
		protected java.awt.Image getImage(){
			Checker.notNull( "field:image", image );
			return this.image;
		}
		
		protected void setImage( final java.awt.Image image ){
			Checker.notNull( "parameter:image", image );
			this.image = image;
		}
		
		/**
		 * The original resource name used to identify and locate the file.
		 */
		private String resourceName;
		
		public String getResourceName(){
			Checker.notEmpty( "field:resourceName", resourceName );
			return this.resourceName;
		}
		
		public void setResourceName( final String resourceName ){
			Checker.notEmpty( "parameter:resourceName", resourceName );
			this.resourceName = resourceName;
		}
		
		public String getFileExtension(){
			final String resourceName = this.getResourceName();
			final int extensionIndex = resourceName.lastIndexOf('.');
			if( -1 == extensionIndex ){
				throw new IllegalStateException( "Unable to retrieve file extension from \"" + resourceName + "\"." );
			}
			
			return resourceName.substring( extensionIndex + 1 );
		}		
		
		public String toString(){
			return super.toString() + ", name: \"" + resourceName + "\".";
		}				
	}
	
	/**
	 * Checks the method for the rocket.client.widget.ImageFactory.location annotation and if it is present tests if it the value is server.
	 * @param method
	 * @return
	 */
	protected boolean shouldAlwaysRequestImageFromServer( final Method method ){
		boolean server = false;
		
		while( true ){
			final List values = method.getMetadataValues( Constants.LOCATION );
			if( null == values || values.size() == 0 ){
				this.throwLocationAnnotationMissing( method );				
			}
			if( values.size() != 1 ){
				this.throwLocationAnnotationFoundMoreThanOnce( method, values );
			}
			
			final String value = (String)values.get( 0 );
			if( Constants.LOCATION_SERVER.equals( value)){
				server = true;
				break;
			}
			if( Constants.LOCATION_LOCAL.equals( value)){
				server = false;
				break;
			}
			this.throwInvalidLocationValue( method, value );
			break;
		}
		
		return server;
	}
	
	protected void throwLocationAnnotationMissing( final Method method ){
		this.throwException( new ImageFactoryGeneratorException( "The \"" + Constants.LOCATION + "\" annotation is missing from the method: \"" + this.toString( method) + "\"."));
	}
	
	protected void throwLocationAnnotationFoundMoreThanOnce( final Method method, final List values ){
		this.throwException( new ImageFactoryGeneratorException( "The \"" + Constants.LOCATION + "\" annotation contains more than one value (" + values + ") on the method: \"" + this.toString( method) + "\"."));
	}
	
	protected void throwInvalidLocationValue( final Method method, final String value ){
		this.throwException( new ImageFactoryGeneratorException( "The method " + method + " contains an invalid \"" + Constants.LOCATION + "\" value of \"" + value + "\"."));
	}
	
	/**
	 * Tests if a server image should be prefetched or lazy loaded upon first request.
	 * @param method
	 * @return
	 */
	protected boolean shouldPrefetchServerImage( final Method method ){
		boolean eager = false;
		
		while( true ){
			final List values = method.getMetadataValues( Constants.SERVER_REQUEST );
			if( null == values || values.size() == 0 ){
				this.throwServerRequestAnnotationMissing( method );
			}
			
			if( values.size() != 1 ){
				this.throwServerRequestAnnotationFoundMoreThanOnce( method, values );
			}
			
			final String value = (String)values.get( 0 );
			if( Constants.SERVER_REQUEST_EAGER.equals( value)){
				eager = true;
				break;
			}
			if( Constants.SERVER_REQUEST_LAZY.equals( value)){
				eager = false;
				break;
			}
			this.throwInvalidLocationValue( method, value );
			break;
		}		
		
		return eager;
	}
	
	protected void throwServerRequestAnnotationMissing( final Method method ){
		this.throwException( new ImageFactoryGeneratorException( "The \"" + Constants.SERVER_REQUEST + "\" annotation is missing from the method: \"" + this.toString( method) + "\"."));
	}
	
	protected void throwServerRequestAnnotationFoundMoreThanOnce( final Method method, final List values ){
		this.throwException( new ImageFactoryGeneratorException( "The \"" + Constants.SERVER_REQUEST + "\" annotation contains more than one value (" + values + ") on the method: \"" + this.toString( method) + "\"."));
	}
	
	protected void throwInvalidServerValue( final Method method, final String value ){
		this.throwException( new ImageFactoryGeneratorException( "The method " + method + " contains an invalid \"" + Constants.SERVER_REQUEST + "\" value of \"" + value + "\"."));
	}

	/**
	 * Overrides the getPreloadUrls method to return a comma separated list of urls.
	 * @param newType
	 * @param methods
	 */
	protected void overloadGetPreloadUrls( final NewConcreteType newType, final Set preFetchUrls ){
		Checker.notNull("parameter:newType", newType );
		Checker.notNull("parameter:preFetchUrls", preFetchUrls );
		
		final GeneratorContext context = this.getGeneratorContext();
		final String methodName = Constants.GET_PRELOAD_URLS;
		context.branch();
		context.info("Overriding " + methodName + " to preload.");

		final Method method = newType.findMostDerivedMethod( methodName, Collections.EMPTY_LIST );
		final NewMethod newMethod = method.copy( newType );
		newMethod.setAbstract( false );
		newMethod.setFinal( true );
		
		final CodeBlock commaSeparatedList = new CodeBlock(){
			public boolean isEmpty(){
				return false;
			}
			public void write( final SourceWriter writer ){
				writer.print( "return ");
			
				final StringBuffer commaSeparated = new StringBuffer();
				
				final Iterator i = preFetchUrls.iterator();
				while( i.hasNext() ){
					final String url = (String) i.next();
					commaSeparated.append( url );
					if( i.hasNext() ){
						commaSeparated.append( ',' );
					}
				}				
				
				new StringLiteral( commaSeparated.toString() ).write(writer);				
				writer.println( ";");
			}
		};
		newMethod.setBody(commaSeparatedList);
			
		final Iterator i = preFetchUrls.iterator();
		while( i.hasNext() ){
			final String url = (String) i.next();
			context.debug( url );
		}
		
		context.unbranch();
	}
	
	/**
	 * Sub classes need to report the maximum data url size.
	 * @return
	 */
	abstract protected int getMaximumDataUrlLength();
	
	/**
	 * Helper which fetches the maximum data url length from a gwt property.
	 * @param property
	 * @return
	 */
	protected int getMaximumDataUrlLength( final String property, final int defaultValue ){
		int maxDataUrl = defaultValue;
		final String value = System.getProperty( property );
		if( false == Tester.isNullOrEmpty( value )){
			try{
				maxDataUrl = Integer.parseInt( value );
			} catch ( final NumberFormatException bad ){
				throwException( new ImageFactoryGeneratorException("Unable to retrieve the numerical value of the system property \"" + property + "\"."));
			}
		}
		return maxDataUrl;
	}
	
	protected void throwException(final ImageFactoryGeneratorException exception) {
		throw exception;
	}
	
	protected String toString( final Method method ){
		return method.getEnclosingType().getName() + "." + method.getName();
	}
}
