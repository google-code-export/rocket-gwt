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
package rocket.widget.test.life.client;

import rocket.util.client.Colour;
import rocket.widget.client.BlockyPixel;
import rocket.widget.client.Life;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LifeTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                caught.printStackTrace();
                Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
            }
        });
        final RootPanel panel = RootPanel.get();

        panel.add(new Label("Interval"));
        final TextBox interval = new TextBox();
        interval.setText("1000");
        panel.add(interval);
        panel.add(new HTML("<br/>"));

        panel.add(new Label("Rows"));
        final TextBox rows = new TextBox();
        rows.setText("30");
        panel.add(rows);
        panel.add(new HTML("<br/>"));

        panel.add(new Label("Columns"));
        final TextBox columns = new TextBox();
        columns.setText("30");
        panel.add(columns);
        panel.add(new HTML("<br/>"));

        panel.add(new Label("CellBias (higher creates more live cells)"));
        final TextBox cellBias = new TextBox();
        cellBias.setText("-123456789");
        panel.add(cellBias);
        panel.add(new HTML("<br/>"));

        final Button button = new Button("Start");
        panel.add(button);
        panel.add(new HTML("<br/>"));

        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                final BlockyPixel pixels = new BlockyPixel();
                pixels.setRows(Integer.parseInt(rows.getText()));
                pixels.setColumns(Integer.parseInt(columns.getText()));
                pixels.setSize("90%", "75%");
                pixels.clear(new Colour(0xdddddd));
                panel.add(pixels);

                final Life life = new Life();
                life.setDeadCellColour(new Colour(0xdddddd));
                life.setLiveCellColour(new Colour(0xbbbbbb));
                life.setPixelGrid(pixels);
                life.createCells(Integer.parseInt(cellBias.getText()));

                final TestTimer timer = new TestTimer();
                timer.setCounter(0);
                timer.setLife(life);
                timer.scheduleRepeating(Integer.parseInt(interval.getText()));

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

    class TestTimer extends Timer {
        public void run() {
            this.update();
            this.setCounter(this.getCounter() + 1);
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

                RootPanel.get().add(
                        new HTML("<b>Test Finished</b><br/>TimeTaken: " + timeTaken + " millis<br/>redraws: " + counter
                                + "<br/>fps: " + fps));
            }
        }

        public void update() {
            final Life life = this.getLife();
            if (life.update() == 0) {
                life.createCells(-1234567890);
                life.update();
            }
        }

        private Life life;

        public Life getLife() {
            return life;
        }

        public void setLife(final Life life) {
            this.life = life;
        }

        private long startTime;

        public void setStartTime(final long startTime) {
            this.startTime = startTime;
        }

        public long getStartTime() {
            return this.startTime;
        }

        private int counter;

        public void setCounter(final int counter) {
            this.counter = counter;
        }

        public int getCounter() {
            return this.counter;
        }
    }
}
