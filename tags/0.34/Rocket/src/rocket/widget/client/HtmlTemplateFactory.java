package rocket.widget.client;

import com.google.gwt.user.client.ui.CheckBox;

/**
 * Base interface for all generated HtmlTemplates. Sub interfaces must define
 * one or more methods. Each method must satisfy the following rules.
 * <ul>
 * <li>Return type must be a sub-class of Widget</li>
 * <li>An id annotation must be present for each getter</li>
 * </ul>
 * 
 * The parent panel of all widgets retrieved from this factory is the RootPanel
 * singleton.
 * 
 * The label portion of the CheckBox, RadioButton widget is not accessible thus
 * attempts to invoke any related method will fail.
 * <ul>
 * <li>{@link CheckBox#getText }</li>
 * <li>{@link CheckBox#setText }</li>
 * <li>{@link CheckBox#getHTML }</li>
 * <li>{@link CheckBox#setHTML }</li>
 * </ul>
 * 
 * @author Miroslav Pokorny
 */
public interface HtmlTemplateFactory {

}
