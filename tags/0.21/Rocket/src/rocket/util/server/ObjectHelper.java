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
package rocket.util.server;

/**
 * A collection of useful methods when working with objects in general.
 * 
 * @author Miroslav Pokorny
 * @version 1.0
 */
public class ObjectHelper extends rocket.util.client.ObjectHelper {

    /**
     * Return the default java.lang.Object.toString() for the given object.
     * 
     * @param object
     *            The object to format
     * @return String the default format representation of the given object. className - the at sign - the Objects hashcode ( in hex form
     *         without the leading '0x' ) java.lang.Object@123def
     */
    public static String defaultToString(final Object object) {
        return object == null ?
        /* handle null */
        String.valueOf(object) :
        /* class name including the leading package name */
        object.getClass().getName() + '@' +
        /* hashcode */
        Integer.toHexString(System.identityHashCode(object));
    } // defaultToString

    protected ObjectHelper() {
    }
}