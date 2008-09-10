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
package rocket.widget.client;

/**
 * This factory offers a number of options primarily making it possible to
 * automatically benefit from data urls for supporting browsers. In IE all
 * images will be fetched from the server whilst for other browsers images may
 * be created from data urls or fetched from the server depending on the size of
 * the base64 encoded form of the image file.
 * 
 * It does not attempt to do any of the magic found in ImageBundles.
 * 
 * Several annotations may be used on each method to control various behavioural
 * aspects. Some annotations are ignored
 * (rocket.widget.client.ImageFactory.serverRequest) when a image is sourced
 * from a data url. <table>
 * <tr>
 * <th>Annotation</th>
 * <th>Description</th>
 * <th>Mandatory</th>
 * </tr>
 * <tr>
 * <td>file</td>
 * <td>A location of the image as a class path resource. Relative paths are
 * relative to the Interface package.</td>
 * <td>yes</td>
 * <tr>
 * <tr>
 * <td>location
 * <td>
 * <td>
 * <ul>
 * <li>local - images use data urls wherever possible/supported.</li>
 * <li>server - urls are always server urls, never data urls</li>
 * </ul>
 * </td>
 * <tr>
 * <tr>
 * <td>serverRequest</td>
 * <td>Controls whether or not server images are loaded eagerly or lazily.
 * <ul>
 * <li>eager - server images are prefetched</li>
 * <li>lazy - The iamge url is fetched when the first image is created.</li>
 * </ul>
 * </td>
 * </tr>
 * </table>
 * 
 * The ImageFactory defines 4 separate system properties that contain the
 * maximum length supported for a particular browser. In the case of IE which
 * doesnt support data urls this value is 0.
 * 
 * The names of these system properties are as follows with defaults included..
 * <ul>
 * <li>rocket.widget.client.ImageFactory.FireFox.maxDataUrlLength=32768</li>
 * <li>rocket.widget.client.ImageFactory.InternetExplorer.maxDataUrlLength=0</li>
 * <li>rocket.widget.client.ImageFactory.Opera.maxDataUrlLength=4000</li>
 * <li>rocket.widget.client.ImageFactory.Safari.maxDataUrlLength=32768</li>
 * <ul>
 * 
 * Doing some testing and googling it seems that firefox and safari dont have
 * any limits on data uris. If you wish to change this overwrite their values by
 * including the appropriate system properties along with the new value.
 * 
 * The example below increases the data uri limit for Firefox to 64K.
 * 
 * <pre>
 * &lt;set-property name=&amp;rocket.widget.client.ImageFactory.FireFox.maxDataUrlLength&amp; value=&amp;65536&amp; /&gt;
 * </pre>
 * 
 * @author Miroslav Pokorny
 */
public interface ImageFactory {

}
