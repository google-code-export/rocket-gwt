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
package rocket.test.widget.slider.client;

import rocket.client.widget.slider.HorizontalSlider;
import rocket.client.widget.slider.VerticalSlider;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
/**
 * Tests both Horizontal and Vertical Slider widgets.
 * @author Miroslav Pokorny (mP)
 */
public class SliderTest implements EntryPoint {

	final int BIG_DELTA = 10;
	final int DELTA = 1;
	final int VALUE = 50;
	final int MAXIMUM_VALUE = 100;
	
	
    public void onModuleLoad() {
        try {
           final RootPanel panel = RootPanel.get();
        	final Label horizontalSliderValue = new Label(String.valueOf( VALUE ));

           final HTML horizontalSliderThumb = new HTML( "&nbsp;");
           horizontalSliderThumb.addStyleName( "thumb");
           horizontalSliderThumb.setSize( "14px", "14px");

           final HorizontalSlider horizontalSlider = new HorizontalSlider();
           horizontalSlider.setSize( "300px", "16px");
           horizontalSlider.setBigDelta( BIG_DELTA );
           horizontalSlider.setDelta( DELTA );
           horizontalSlider.setMaximumValue( MAXIMUM_VALUE );
           horizontalSlider.setWidget( horizontalSliderThumb );
           DOM.setStyleAttribute(horizontalSliderThumb.getElement(), "top", "1px");
           
           horizontalSlider.addChangeListener( new ChangeListener(){
        	   public void onChange(Widget sender){
        		   horizontalSliderValue.setText( "" + horizontalSlider.getValue() );
        	   }
           });
           horizontalSlider.setValue( VALUE );
        
           
       	final Label verticalSliderValue = new Label( String.valueOf( VALUE ));
        
        final HTML verticalSliderThumb = new HTML( "&nbsp;");
        verticalSliderThumb.addStyleName( "thumb");
        verticalSliderThumb.setSize( "14px", "14px");

        final VerticalSlider verticalSlider = new VerticalSlider();
        verticalSlider.setSize( "14px", "300px" );
        verticalSlider.setBigDelta( BIG_DELTA );
        verticalSlider.setDelta( DELTA );
        verticalSlider.setMaximumValue( MAXIMUM_VALUE );
        verticalSlider.setWidget( verticalSliderThumb );
        DOM.setStyleAttribute(verticalSliderThumb.getElement(), "left", "1px");
        
        verticalSlider.addChangeListener( new ChangeListener(){
     	   public void onChange(Widget sender){
     		   verticalSliderValue.setText( "" + verticalSlider.getValue() );
     	   }
        });
        panel.add( new HTML("Value"));
        verticalSlider.setValue( VALUE );
        panel.add( new HTML("<br/>"));
        
        final TextBox bigDelta = new TextBox();
        bigDelta.setText( String.valueOf( BIG_DELTA ));
        bigDelta.addKeyboardListener( new KeyboardListenerAdapter(){
        	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        		if( KeyboardListener.KEY_ENTER == keyCode ){
        			final int newBigDelta = Integer.parseInt( bigDelta.getText() );
        			horizontalSlider.setBigDelta( newBigDelta );
        			verticalSlider.setBigDelta( newBigDelta );
        		}
        	 }
        });
        panel.add( new HTML("Big Delta"));
        panel.add( bigDelta );
        panel.add( new HTML("<br/>"));
        
        final TextBox delta = new TextBox();
        delta.setText( "1");
        delta.addKeyboardListener( new KeyboardListenerAdapter(){
        	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        		if( KeyboardListener.KEY_ENTER == keyCode ){
        			final int newDelta = Integer.parseInt( delta.getText() );
        			horizontalSlider.setDelta( newDelta );
        			verticalSlider.setDelta( newDelta);
        		}
        	 }
        });
        panel.add( new HTML("Delta"));
        panel.add( delta );
        panel.add( new HTML("<br/>"));

        final TextBox value = new TextBox();
        value.setText( String.valueOf( VALUE ));
        value.addKeyboardListener( new KeyboardListenerAdapter(){
        	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        		if( KeyboardListener.KEY_ENTER == keyCode ){
        			final int newValue = Integer.parseInt( value.getText() );
        			horizontalSlider.setValue( newValue );
        			verticalSlider.setValue( newValue);
        		}
        	 }
        });        
        panel.add( new HTML("Value"));
        panel.add( value );
        panel.add( new HTML("<br/>"));
        
        final TextBox maximumValue = new TextBox();
        maximumValue.setText( String.valueOf( MAXIMUM_VALUE ));
        maximumValue.addKeyboardListener( new KeyboardListenerAdapter(){
        	public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        		if( KeyboardListener.KEY_ENTER == keyCode ){
        			final int newMaximumValue = Integer.parseInt( maximumValue.getText() );
        			horizontalSlider.setMaximumValue( newMaximumValue );
        			verticalSlider.setMaximumValue( newMaximumValue);
        		}
        	 }
        });        
        panel.add( new HTML("Maximum value"));
        panel.add( maximumValue );
        panel.add( new HTML("<br/>"));
        
        panel.add( horizontalSliderValue );
        panel.add( horizontalSlider );
        panel.add( verticalSliderValue );
        panel.add( verticalSlider );

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
