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
package rocket.util.test;

import java.awt.Color;

import junit.framework.TestCase;
import rocket.util.client.Colour;
import rocket.util.client.HueSaturationValue;
import rocket.util.client.Tester;

public class HueSaturationValueTestCase extends TestCase {

	public void testAsColour() {
		for (int red = 0; red < 255; red++) {
			for (int green = 0; green < 255; green++) {
				for (int blue = 0; blue < 255; blue++) {
					final Color color = new Color(red, green, blue);
					final float[] hsvValues = Color.RGBtoHSB(red, green, blue, null);

					final HueSaturationValue hsv = new HueSaturationValue(hsvValues[0], hsvValues[1], hsvValues[2]);

					final Colour colour = hsv.asColour();
					if (false == equals(color, colour)) {
						fail("expected: " + color + ", actual: " + colour);
					}
				}
			}
		}
	}

	static boolean equals(final Color awtColor, final Colour colour) {
		boolean equals = false;
		while (true) {
			if (false == Tester.equals(awtColor.getRed(), colour.getRed(), 5)) {
				break;
			}
			if (false == Tester.equals(awtColor.getGreen(), colour.getGreen(), 5)) {
				break;
			}
			if (false == Tester.equals(awtColor.getBlue(), colour.getBlue(), 5)) {
				break;
			}
			equals = true;
			break;
		}
		return equals;
	}
}
