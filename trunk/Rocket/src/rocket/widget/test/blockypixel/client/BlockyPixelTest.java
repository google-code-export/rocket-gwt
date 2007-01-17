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
package rocket.widget.test.blockypixel.client;

import java.util.Date;

import rocket.style.client.StyleConstants;
import rocket.util.client.Colour;
import rocket.widget.client.BlockyPixel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class BlockyPixelTest implements EntryPoint {

    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                caught.printStackTrace();
                Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
            }
        });
        final RootPanel panel = RootPanel.get();

        panel.add(new HTML("Interval between repaints(ms)"));
        final TextBox interval = new TextBox();
        interval.setText("100");
        panel.add(interval);
        panel.add(new HTML("<br/>"));

        panel.add(new HTML("Rows"));
        final TextBox rows = new TextBox();
        rows.setText("10");
        panel.add(rows);
        panel.add(new HTML("<br/>"));

        panel.add(new HTML("Columns"));
        final TextBox columns = new TextBox();
        columns.setText("10");
        panel.add(columns);
        panel.add(new HTML("<br/>"));

        final Button button = new Button("Start");
        panel.add(button);

        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                final BlockyPixel grid = new BlockyPixel();
                grid.setRows(Integer.parseInt(rows.getText()));
                grid.setColumns(Integer.parseInt(columns.getText()));
                grid.setSize("90%", "75%");

                DOM.setIntStyleAttribute(grid.getElement(), StyleConstants.Z_INDEX, 100);
                panel.add(grid, 0, 0);

                // DOM.scrollIntoView(grid.getElement());

                final TestTimer timer = new TestTimer();
                timer.setCounter(0);
                timer.setGrid(grid);
                timer.scheduleRepeating(Integer.parseInt(interval.getText()));

                log("<b>Test started</b>");

                final int columns = grid.getColumns();
                final int rows = grid.getRows();
                log("columns: " + columns + ", rows: " + rows);
                log("time: " + new Date() + "</b>");

                DOM.addEventPreview(new EventPreview() {
                    public boolean onEventPreview(final Event event) {
                        if (DOM.eventGetType(event) == Event.ONCLICK) {
                            timer.cancel();

                            DOM.removeEventPreview(this);
                        }
                        return true;
                    }
                });
            }
        });
    }

    void log(final String message) {
        Element log = DOM.getElementById("log");
        DOM.setInnerHTML(log, DOM.getInnerHTML(log) + message + "<br>");
    }

    /**
     * This timer is in charge of periodically updating the colours appearing in the grid as well as updating counters.
     * 
     * @author mP
     */
    class TestTimer extends Timer {
        public void run() {
            final int counter = this.getCounter();
            final float mix = (counter % 10) / (float) 10;
            this.update(mix);
            this.setCounter(counter + 1);
        }

        public void scheduleRepeating(int interval) {
            setStartTime(System.currentTimeMillis());
            super.scheduleRepeating(interval);
        }

        public void cancel() {
            super.cancel();

            final int counter = this.getCounter();
            if (counter > 0) {
                final long timeTaken = System.currentTimeMillis() - this.getStartTime();

                final float fps = counter * 1000 / (float) timeTaken;
                log("<b>Test stopped</b>");
                log("time: " + new Date());
                log("timeTaken: " + timeTaken + "millis, redraws: " + counter + ", frames per second: " + fps);

                RootPanel.get().remove(this.getGrid());
            }
        }

        public void update(final float whiteMix) {
            final BlockyPixel grid = this.getGrid();

            final int rows = grid.getRows();
            final int columns = grid.getColumns();

            for (int x = 0; x < columns; x++) {
                final int red = (int) ((float) x * 255 / columns);

                for (int y = 0; y < rows; y++) {
                    final int green = (int) ((float) y * 255 / rows);
                    final int blue = (red ^ green);
                    final Colour colour = new Colour(red, green, blue).makeLighter(whiteMix);
                    grid.setColour(x, y, colour);
                }
            }
        }

        private BlockyPixel grid;

        public BlockyPixel getGrid() {
            return grid;
        }

        public void setGrid(final BlockyPixel grid) {
            this.grid = grid;
        }

        private long startTime;

        public void setStartTime(final long startTime) {
            this.startTime = startTime;
        }

        public long getStartTime() {
            return this.startTime;
        }

        /**
         * The number of redraws
         */
        private int counter;

        public void setCounter(final int counter) {
            this.counter = counter;
        }

        public int getCounter() {
            return this.counter;
        }
    }
}
