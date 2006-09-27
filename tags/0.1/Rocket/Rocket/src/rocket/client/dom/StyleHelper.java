/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.dom;

import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A variety of helper methods related to css/stylesheets and widgets/html.
 *
 * This helper provides support for changing styles/classes for widgets that use a heirarchical manner to name
 * their composite widgets/elements.
 *
 * @author Miroslav Pokorny (mP)
 */
public class StyleHelper{

	final static char COMPOUND = '-';
	final static char SEPARATOR_CHAR = ' ';
	final static String SEPARATOR = " ";
	final static String CLASSNAME = "className";

    /**
     * Creates an array which contains the individual class names
     * @param classes
     * @return
     */
    protected static String[] toArray( final String classes ){
        return StringHelper.split(classes, SEPARATOR, true);
    }

    /**
     * Joins the array of classnames ignoring null elements.
     * @param classes
     * @return
     */
    protected static String join( final String[] classes ){
        return StringHelper.join( classes, SEPARATOR );
    }

    public static void checkClassNames( final String name, final String classes ){
        StringHelper.checkNotNull( name, classes );
    }

    /**
     * Checks taht the given className contains a single valid classname
     * @param name
     * @param className
     */
    public static void checkClassName( final String name, final String className ){
        if( StringHelper.isNullOrEmpty( className )){
            ObjectHelper.handleNullEncountered( name, className );
        }
    }
	/**
	 * Concatenates or builds a complete stylename given a prefix and a suffix.
	 * @param prefix
	 * @param suffix
	 * @return
	 */
    public static String buildCompound(final String prefix, final String suffix) {
		return prefix + COMPOUND + suffix;
	}

    protected static String[] buildCompounds( final String prefixes, final String suffix ){
        StringHelper.checkNotEmpty( "parameter:prefixes", prefixes );
        ObjectHelper.checkNotNull( "parameter:suffix", suffix );

        final String[] array = toArray( prefixes );
        for( int i = 0; i < array.length; i++ ){
            array[ i ] = buildCompound( array[ i ], suffix );
        }
        return array;
    }

    protected static String removeDuplicates( final String classNames ){
        checkClassNames( "parameter:classNames", classNames );

        int i = 0;
        final String[] classesArray = toArray( classNames );
        for( int j = 0; j < classesArray.length; j++ ){
            final String classs = classesArray[ j ];

            for( int k = 0; j < i; k++ ){
                // duplicate found null it!
                if( classs.equals( classesArray[ k ] )){
                    classesArray[ j ] = null;
                    i++;
                    break;
                }
            }
        }

        // $4i will be 0 if no duplicate are found as an optimisation simply return $classNames w/out joining $classesArray
        return i == 0 ? classNames : join( classesArray );
    }

    protected static void nullDuplicates( final String[] classNames ){
        ObjectHelper.checkNotNull( "parameter:classNames", classNames );

        int i = 0;
        for( int j = 0; j < classNames.length; j++ ){
            final String classs = classNames[ j ];

            for( int k = 0; j < i; k++ ){
                // duplicate found null it!
                if( classs.equals( classNames[ k ] )){
                    classNames[ j ] = null;
                    i++;
                    break;
                }
            }
        }
    }

    protected static String get( final Element element ){
        ObjectHelper.checkNotNull("parameter:element", element);

        final String value = DOM.getAttribute(element, CLASSNAME);
        StringHelper.checkNotNull( "elementClassname", value );
        return value;
    }

    protected static boolean has( final Element element ){
        ObjectHelper.checkNotNull("parameter:element", element);
        return DOM.getAttribute(element, CLASSNAME) != null;
    }

    // SET ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public static String set( final String classNames ){
        return removeDuplicates( classNames );
    }
    /**
     * Simply saves the given classNames String upon the given element
     * No attempt is made to validate if duplicate classNames are included etc.
     * @param element
     * @param classNames
     */
    public static void set( final Element element, final String classNames ){
        ObjectHelper.checkNotNull("parameter:element", element);
        StringHelper.checkNotNull( "parameter:classNames", classNames);

        DOM.setAttribute(element, CLASSNAME, removeDuplicates( classNames));
    }

    // ADD ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public static String add( final String oldClassNames, final String newClassNames ){
        StringHelper.checkNotNull( "parameter:oldClassNames", oldClassNames);
        StringHelper.checkNotNull( "parameter:newClassNames", newClassNames);

        return removeDuplicates( oldClassNames + SEPARATOR + newClassNames );
    }

    /**
     * Adds the given classNames to the given element.
     * No attempt is made to validate if duplicate classNames are included etc.
     * @param element
     * @param classNames
     */
    public static void add( final Element element, final String classNames ){
        ObjectHelper.checkNotNull("parameter:element", element);
        StringHelper.checkNotNull( "parameter:classNames", classNames);

        set( element, get(element) + SEPARATOR + classNames );
    }

// REMOVE ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public static String remove( final String classNames, final String removeClassNames ){
        StringHelper.checkNotNull( "parameter:classNames", classNames);
        StringHelper.checkNotNull( "parameter:removeClassNames", removeClassNames);


        final String[] classNamesArray = toArray( classNames );
        final String[] removeClassNamesArray = toArray( removeClassNames );
        for( int i = 0; i < removeClassNamesArray.length; i++ ){
            final String remove = removeClassNamesArray[ i ];

            for( int j = 0; j < classNamesArray.length; j++ ){
                if( remove.equals( classNamesArray[ j ])){
                    classNamesArray[ i ]= null;
                    break;
                }
            }
        }

        return removeDuplicates( join( classNamesArray ));
    }

    /**
     * Adds the given classNames to the given element.
     * No attempt is made to validate if duplicate classNames are included etc.
     * @param element
     * @param classNames
     */
    public static void remove( final Element element, final String classNames ){
        ObjectHelper.checkNotNull("parameter:element", element);
        StringHelper.checkNotNull( "parameter:classNames", classNames);

        set( element, remove( get(element), classNames ));
    }

    public static void remove( final Element element, final String prefixes, final String suffix ){
        ObjectHelper.checkNotNull("parameter:element", element);
        StringHelper.checkNotEmpty( "parameter:prefixes", prefixes);
        StringHelper.checkNotEmpty( "parameter:suffix", suffix);

        remove( element, join( buildCompounds( prefixes, suffix )));
    }
}