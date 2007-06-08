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

import java.util.Iterator;

import rocket.util.client.Colour;
import rocket.util.client.ObjectHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * This Helper contains a number of useful methods related to working with GWT widgets and the browser in general.
 * 
 * This helper also contains a number of factories for creating various google widgets and should be used as they attempt to fix various
 * issues/bugs within GWT. Sometimes this is as simple as setting a default styleName.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class WidgetHelper extends SystemHelper {

    /**
     * Given an element attempts to find which widget it is a child of. This is particularly useful when a panel contains many widgets which
     * in themselves are made up of many elements and one needs to determine which widget the event belongs too.
     * 
     * @param target
     * @param widgets
     * @return The widget or null if a match was not possible.
     */
    public static Widget findWidget(final Element target, final Iterator widgets) {
        ObjectHelper.checkNotNull("parameter:target", target);
        ObjectHelper.checkNotNull("parameter:widgets", widgets);

        Widget widget = null;
        while (widgets.hasNext()) {
            final Widget otherWidget = (Widget) widgets.next();
            if (DOM.isOrHasChild(otherWidget.getElement(), target)) {
                widget = otherWidget;
                break;
            }
        }
        return widget;
    }

    /**
     * Fills the given window with a gradient colour fill.
     * 
     * @param grid
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param fromColour
     * @param toColour
     */
    public static void verticalGradientFill(final PixelGrid grid, final int left, final int top, final int right,
            final int bottom, final Colour fromColour, final Colour toColour) {
        ObjectHelper.checkNotNull("parameter:grid", grid);
        ObjectHelper.checkNotNull("parameter:fromColour", fromColour);
        ObjectHelper.checkNotNull("parameter:bottom", toColour);

        final int rowsBetween = bottom - top;
        final float rowColourMixDelta = -1.0f / rowsBetween;
        float rowColourMixRatio = 1.0f + rowColourMixDelta / 2;

        for (int y = top; y < bottom; y++) {
            final Colour rowColour = fromColour.mix(toColour, rowColourMixRatio);

            for (int x = left; x < right; x++) {
                grid.setColour(x, y, rowColour);
            }

            rowColourMixRatio = rowColourMixRatio + rowColourMixDelta;
        }
    }

    public static void horizontalGradientFill(final PixelGrid grid, final int left, final int top, final int right,
            final int bottom, final Colour fromColour, final Colour toColour) {
        gradientFill(grid, left, top, right, bottom, fromColour, toColour, fromColour, toColour);
    }

    /**
     * Fills the given window within the given grid with a mixture of the colours given.
     * 
     * @param grid
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param topLeftColour
     * @param topRightColour
     * @param bottomLeftColour
     * @param bottomRightColour
     */
    public static void gradientFill(final PixelGrid grid, final int left, final int top, final int right,
            final int bottom, final Colour topLeftColour, final Colour topRightColour, final Colour bottomLeftColour,
            final Colour bottomRightColour) {

        ObjectHelper.checkNotNull("parameter:grid", grid);

        final int rowsBetween = bottom - top;
        final float rowColourMixDelta = -1.0f / rowsBetween;
        float rowColourMixRatio = 1.0f + rowColourMixDelta / 2;

        final int columnsAcross = right - left;
        final float columnDelta = -1.0f / columnsAcross;

        for (int y = top; y < bottom; y++) {
            final Colour leftEdgeColour = topLeftColour.mix(bottomLeftColour, rowColourMixRatio);
            final Colour rightEdgeColour = topRightColour.mix(bottomRightColour, rowColourMixRatio);

            float columnColourMixRatio = 1.0f + columnDelta / 2;

            for (int x = left; x < right; x++) {
                final Colour cellColour = leftEdgeColour.mix(rightEdgeColour, columnColourMixRatio);
                grid.setColour(x, y, cellColour);

                columnColourMixRatio = columnColourMixRatio + columnDelta;
            }
            rowColourMixRatio = rowColourMixRatio + rowColourMixDelta;
        }
    }
}