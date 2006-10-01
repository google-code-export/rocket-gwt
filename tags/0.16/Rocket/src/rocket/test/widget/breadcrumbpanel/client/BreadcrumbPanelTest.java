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
package rocket.test.widget.breadcrumbpanel.client;

import rocket.client.widget.BreadcrumbPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BreadcrumbPanelTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        try {
            final RootPanel rootPanel = RootPanel.get();
            final BreadcrumbPanel breadcrumbs = new BreadcrumbPanel();
            breadcrumbs.push( "First", new ClickListener(){
            	public void onClick( final Widget sender ){
            		Window.alert( "First breadcrumb clicked");
            	}
            });

            breadcrumbs.push( "Second", new ClickListener(){
            	public void onClick( final Widget sender ){
            		Window.alert( "Second breadcrumb clicked");
            	}
            });
            breadcrumbs.push( "Third", new ClickListener(){
            	public void onClick( final Widget sender ){
            		Window.alert( "Third breadcrumb clicked");
            	}
            });
            rootPanel.add( breadcrumbs);

            final Button push = new Button( "Push another breadcrumb onto the panel.");
            push.addClickListener( new ClickListener(){
            	public void onClick( final Widget ignore ){
            		final String text = "breadcrumb-" + System.currentTimeMillis();

            		breadcrumbs.push( text, new ClickListener(){
                       	public void onClick( final Widget sender ){
                    		Window.alert( "breadcrumb [" + text + "]clicked");
                    	}
            		});
            	}
            });
            rootPanel.add( push );

            final Button pop = new Button( "Pop last breadcrumb");
            pop.addClickListener( new ClickListener(){
                public void onClick( final Widget ignore ){
                    breadcrumbs.pop();
                }
            });
            rootPanel.add( pop );

            final Button clear = new Button( "Clear all breadcrumbs");
            clear.addClickListener( new ClickListener(){
                public void onClick( final Widget ignore ){
                    breadcrumbs.clear();
                }
            });
            rootPanel.add( clear );

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
