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
package rocket.serialization.rebind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.ConcreteTypesImplementingInterfaceVisitor;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

/**
 * This class is responsible for attempting to find the best ObjectReader/Writer
 * for each of the given types.
 */
abstract public class ObjectReaderOrWriterFinder {

	/**
	 * Builds up a map that contains every type along with its corresponding
	 * Reader/Writer. Types that are not matched by a Reader/Writer do not
	 * appear in the map.
	 * 
	 * @param types
	 * @return
	 */
	public Map build(final Set types) {
		//SerializationFactoryGenerator.log("--->finding..." + types.toString().replace(',', '\n'));

		final Map accumulator = new HashMap();

		final ConcreteTypesImplementingInterfaceVisitor visitor = new ConcreteTypesImplementingInterfaceVisitor() {
			protected boolean visit(final Type readerOrWriter) {
				final Map matches = ObjectReaderOrWriterFinder.this.findMatchingType(readerOrWriter);
				while( true ){
					// nothing to do...
					if( matches.isEmpty() ){
						break;
					}
					// check if any of the matched types overlap any of the serializedtypes.. if result is none do nothing...
					if( false == ObjectReaderOrWriterFinder.this.findSingleMatch( matches, types )){
						break;
					}
					
					ObjectReaderOrWriterFinder.this.mergeMatches(matches, accumulator);
					break;
				}				
				return false;
			}

			protected boolean skipAbstractTypes() {
				return true;
			}
		};
		final Type readerOrWriterInterface = this.getImplementingInterface();
		visitor.start(readerOrWriterInterface);

		//SerializationFactoryGenerator.log("AFTER REMOVING UNNNECESSARY READERS/WRITERS:::" + accumulator.toString().replace(',', '\n') + "\n===========");

		return this.finalizeBindings(accumulator);
	}

	/**
	 * Sub classes should return either {@link rocket.serialization.client.ObjectReader} or
	 * {@link rocket.serialization.client.ObjectWriter} type.
	 * 
	 * @return
	 */
	abstract protected Type getImplementingInterface();

	/**
	 * Given a reader/writer builds a map of all the types that it should apply
	 * too.
	 */
	protected Map findMatchingType(final Type readerOrWriter) {
		ObjectHelper.checkNotNull("parameter:readerOrWriter", readerOrWriter);

		Map matches = null;

		while (true) {
			final Type type = this.getTypeFromAnnotation(readerOrWriter);
			if (null == type) {
				//SerializationFactoryGenerator.log( "getTypeFromANnotation for " + readerOrWriter + " was missing...");
				matches = Collections.EMPTY_MAP;
				break;
			}
			if (false == this.shouldBeSerialized(type)) {
				matches = Collections.EMPTY_MAP;
				break;
			}

			// interface find all implementing interfaces...
			if (type.isInterface()) {
				matches = this.findTypesImplementingInterface(type, readerOrWriter);
				break;
			}
			// find all sub classes...
			//matches = this.buildMatchingSubTypes(type, readerOrWriter);
			final Match match = new Match();
			match.setScore(ObjectReaderOrWriterFinder.CLASS_MATCH);
			match.setReaderWriter(readerOrWriter);

			//SerializationFactoryGenerator.log( "\texact" + readerOrWriter + "\t" + type );
			matches = new HashMap();
			matches.put(type, match);
			break;
		}

		return matches;
	}

	/**
	 * Builds up a map that contains the concrete types that implement the given
	 * interface.
	 * 
	 * @param interfacee
	 * @param readerOrWriter
	 * @return
	 */
	protected Map findTypesImplementingInterface(final Type interfacee, final Type readerOrWriter) {
		ObjectHelper.checkNotNull("parameter:interfacee", interfacee);
		ObjectHelper.checkNotNull("parameter:readerOrWriter", readerOrWriter);

		final Map matches = new HashMap();

		final ConcreteTypesImplementingInterfaceVisitor visitor = new ConcreteTypesImplementingInterfaceVisitor() {

			protected boolean skip(final Type interfacee, final Type type) {
				boolean skip = super.skip(interfacee, type);

				if (false == skip) {
					skip = !ObjectReaderOrWriterFinder.this.shouldBeSerialized(type);
				}

				return skip;
			}

			protected boolean visit(final Type type) {
				final boolean previouslyVisited = matches.containsKey(type);
				if (false == previouslyVisited) {

					final Match match = new Match();
					match.setScore(ObjectReaderOrWriterFinder.INTERFACE_MATCH);
					match.setReaderWriter(readerOrWriter);

					matches.put(type, match);
				}
				return previouslyVisited;
			}

			protected boolean skipAbstractTypes() {
				return false;
			}
		};
		visitor.start(interfacee);
		return matches;
	}

	/**
	 * Retrieves the type after reading the type name from an annotation
	 * belonging to the given type.
	 * 
	 * @param type
	 *            The type being checked
	 * @return The located type if an annotation exists on the incoming type.
	 */
	protected Type getTypeFromAnnotation(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		Type typeFromAnnotation = null;
		while (true) {
			// annotation not found...
			final List values = type.getMetadataValues(SerializationConstants.SERIALIZABLE_TYPE);
			if (values.size() == 0) {
				break;
			}
			if (values.size() != 1) {
				throw new SerializationFactoryGeneratorException("Type contains more than one " + SerializationConstants.SERIALIZABLE_TYPE
						+ " annotation.");
			}

			// type must be found if annotation exists...
			final String typeName = (String) values.get(0);
			if (null != typeName) {
				typeFromAnnotation = type.getGeneratorContext().getType(typeName);
				ObjectHelper.checkNotNull("Unable to find annotated type" + typeName, typeFromAnnotation);
			}
			break;
		}

		return typeFromAnnotation;
	}


	/**
	 * Loops thru all the types belonging to matches attempting to find at least one type in serializableTypes set.
	 * @param matches
	 * @param serializableTypes
	 * @return
	 */
	protected boolean findSingleMatch( final Map matches, final Set serializableTypes ){
		boolean found = false;
		
		final Iterator iterator = matches.keySet().iterator();
		while( iterator.hasNext() ){
			final Type type = (Type) iterator.next();
			if( serializableTypes.contains( type )){
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	/**
	 * Adds to the list of matches for every type in old matches. If a type
	 * doesnt exist in oldMatch a new entry is created.
	 * 
	 * @param newMatches
	 * @param oldMatches
	 */
	protected void mergeMatches(final Map newMatches, final Map oldMatches) {
		// need to check if any of the types in $newMatch exist in $oldMatches

		final Iterator newMatchesIterator = newMatches.entrySet().iterator();
		while (newMatchesIterator.hasNext()) {
			final Map.Entry entry = (Map.Entry) newMatchesIterator.next();
			final Type newType = (Type) entry.getKey();
			final Match newMatch = (Match) entry.getValue();

			List listOfMatchesForType = (List) oldMatches.get(newType);
			if (null == listOfMatchesForType) {
				listOfMatchesForType = new ArrayList();

				oldMatches.put(newType, listOfMatchesForType);
			}
			// only add if newMatch is not the same type as one of oldMatch's
			final Type newMatchReaderOrWriter = newMatch.getReaderWriter();
			final Iterator iterator = listOfMatchesForType.iterator();
			boolean duplicate = false;
			while (iterator.hasNext()) {
				final Match otherMatch = (Match) iterator.next();
				if (newMatchReaderOrWriter.equals(otherMatch.getReaderWriter())) {
					duplicate = true;
					break;
				}
			}

			if (false == duplicate) {
				listOfMatchesForType.add(newMatch);
			}
		}
	}

	private final static int CLASS_MATCH = 2;

	private final static int INTERFACE_MATCH = 1;

	/**
	 * Instances of this class hold just how good a match an annotation is for a
	 * type. The score property is used to allow reader/writers with an exact
	 * class to take priority over a reader/writer that says it handles
	 * interfaces.
	 * 
	 * eg an exact class match will have a score one higher than a subclass of
	 * the very same class.
	 */
	static private class Match {

		Type readerWriter;

		Type getReaderWriter() {
			ObjectHelper.checkNotNull("field:readerWriter", readerWriter);
			return this.readerWriter;
		}

		void setReaderWriter(final Type readerWriter) {
			ObjectHelper.checkNotNull("parameter:readerWriter", readerWriter);
			this.readerWriter = readerWriter;
		}

		int score;

		int getScore() {
			PrimitiveHelper.checkNotZero("field:score", score);
			return score;
		}

		void setScore(final int score) {
			PrimitiveHelper.checkNotZero("parameter:score", score);
			this.score = score;
		}

		public String toString() {
			return super.toString() + ", type: " + readerWriter + ", score: " + score;
		}
	}

	/**
	 * Comparator which sorts all matches from highest to lowest score.
	 */
	static final Comparator DESCENDING_SCORE_COMPARATOR = new Comparator() {
		public int compare(final Object object, final Object otherObject) {
			return this.compare((Match) object, (Match) otherObject);
		}

		public boolean equals(final Object object, final Object otherObject) {
			return 0 == this.compare((Match) object, (Match) otherObject);
		}

		int compare(final Match match, final Match otherMatch) {
			return otherMatch.getScore() - match.getScore();
		}
	};

	/**
	 * Loops thru all types in the incoming map and then attempts to select the
	 * best match, ie assign a reader/writer for a type.
	 * 
	 * @param matches
	 * @return A map with the type as the key and reader/writer as the value. 
	 */
	protected Map finalizeBindings(final Map matches) {
		ObjectHelper.checkNotNull("parameter:matches", matches);

		final Map finalized = new HashMap();

		final Iterator entries = matches.entrySet().iterator();
		while (entries.hasNext()) {
			final Map.Entry entry = (Map.Entry) entries.next();
			final Type type = (Type) entry.getKey();
			final List matchList = (List) entry.getValue();

			// sort $matchList so the highest score appears first...
			Collections.sort(matchList, ObjectReaderOrWriterFinder.DESCENDING_SCORE_COMPARATOR);

			Match match = (Match) matchList.get(0);
			Type readerWriter = match.getReaderWriter();

			if (matchList.size() > 1) {
				final Match secondMatch = (Match) matchList.get(1);

				final int firstScore = match.getScore();
				final int secondScore = secondMatch.getScore();
				if (firstScore == secondScore) {
					this.throwAmbiguousMatches(type, readerWriter, secondMatch.getReaderWriter());
				}
			}

			finalized.put(type, readerWriter);
		}

		return finalized;
	}

	abstract protected void throwAmbiguousMatches(final Type type, final Type readerOrWriter, final Type secondReaderOrWriter);

	/**
	 * Tests if the given type should be ignored when attempting to match a ObjectReader/ObjectWriter
	 * @return
	 */
	abstract protected boolean shouldBeSerialized(Type type);
}
