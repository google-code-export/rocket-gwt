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

import rocket.dom.client.Dom;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormHandlerCollection;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.impl.FormPanelImpl;
import com.google.gwt.user.client.ui.impl.FormPanelImplHost;

/**
 * A simple widget that contains the same capabilities of the GWT FormPanel but
 * also adds the ability to hijack any elements from the dom. This panel does
 * not include a setWidget/getWidget. In order to replace the only widget it
 * must be first removed and then the new widget added.
 * 
 * Most of the internals have been ripped and reworked from the original GWT
 * FormPanel.
 * 
 * TODO When upgrading GWT version reapply changes to this class.
 * 
 * @author Miroslav Pokorny
 */
public class FormPanel extends Panel {


	/**
	 * Reuse the GWT FormPanel support.
	 */
	static private FormPanelImpl support = createSupport();

	static FormPanelImpl getSupport() {
		return support;
	}
	
	static FormPanelImpl createSupport() {
		return (FormPanelImpl) GWT.create(FormPanelImpl.class);
	}
	
	/**
	 * A generator that is continually incremented to guaranteed that unique
	 * target values are used when invoking {@link #FormPanel()}
	 */
	private static int targetId = 0;

	static String generateTargetId() {
		return WidgetConstants.FORM_PANEL_TARGET_PREFIX + FormPanel.targetId++;
	}

	/**
	 * Creates a new FormPanel. When created using this constructor, it will be
	 * submitted to a hidden &lt;iframe&gt; element, and the results of the
	 * submission made available via {@link FormHandler}.
	 * 
	 * <p>
	 * The back-end server is expected to respond with a content-type of
	 * 'text/html', meaning that the text returned will be treated as HTML. If
	 * any other content-type is specified by the server, then the result html
	 * sent in the onFormSubmit event will be unpredictable across browsers, and
	 * the {@link FormHandler#onSubmitComplete(FormSubmitCompleteEvent)} event
	 * may not fire at all.
	 * </p>
	 * 
	 * @tip The initial implementation of FormPanel specified that the server
	 *      respond with a content-type of 'text/plain'. This has been
	 *      intentionally changed to specify 'text/html' because 'text/plain'
	 *      cannot be made to work properly on all browsers.
	 */
	public FormPanel() {
		super();
	}

	public FormPanel(final Element form, final boolean makeFormElementsIntoWidgets) {
		super(form);

		if (makeFormElementsIntoWidgets) {
			this.makeFormElementsIntoWidgets();
		}
	}

	protected void checkElement(final Element element) {
		Dom.checkTagName("parameter:element", element, WidgetConstants.FORM_TAG);
	}

	protected void beforeCreatePanelElement() {
	}

	protected Element createPanelElement() {
		return DOM.createForm();
	}

	protected String getInitialStyleName() {
		return WidgetConstants.FORM_PANEL_STYLE;
	}

	protected int getSunkEventsBitMask() {
		return 0;
	}

	protected void afterCreatePanelElement() {
		this.setTarget(generateTargetId());
		this.setFormHandlers(this.createFormHandlers());
	}

	/**
	 * This method which is typically called during construction, iterates over
	 * the form element collection and adds/adopts widget wrappers for each form
	 * element.
	 */
	protected void makeFormElementsIntoWidgets() {
		final JavaScriptObject collection = ObjectHelper.getObject(this.getElement(), "elements");
		final int count = ObjectHelper.getPropertyCount(collection);

		for (int i = 0; i < count; i++) {
			com.google.gwt.user.client.ui.Widget widget = null;
			final Element formElement = ObjectHelper.getElement(collection, i);

			while (true) {
				if (Dom.isTag(formElement, WidgetConstants.BUTTON_TAG) || Dom.isInput(formElement, WidgetConstants.BUTTON_INPUT_RESET_TYPE)
						|| Dom.isInput(formElement, WidgetConstants.BUTTON_INPUT_SUBMIT_TYPE)) {
					widget = new Button(formElement);
					break;
				}
				if (Dom.isInput(formElement, WidgetConstants.CHECKBOX_INPUT_TYPE)) {
					widget = new CheckBox(formElement);
					break;
				}
				if (Dom.isInput(formElement, WidgetConstants.FILE_UPLOAD_INPUT_TYPE)) {
					widget = new FileUpload(formElement);
					break;
				}
				if (Dom.isInput(formElement, WidgetConstants.HIDDEN_INPUT_TYPE)) {
					widget = new Hidden(formElement);
					break;
				}
				if (Dom.isTag(formElement, WidgetConstants.LISTBOX_TAG)) {
					widget = new ListBox(formElement);
					break;
				}
				if (Dom.isInput(formElement, WidgetConstants.RADIO_BUTTON_INPUT_TYPE)) {
					widget = new RadioButton(formElement);
					break;
				}
				if (Dom.isInput(formElement, WidgetConstants.PASSWORD_TEXTBOX_INPUT_TYPE)) {
					widget = new TextBox(formElement);
					break;
				}
				if (Dom.isTag(formElement, WidgetConstants.TEXTAREA_TAG)) {
					widget = new TextArea(formElement);
					break;
				}
				if (Dom.isInput(formElement, WidgetConstants.TEXTBOX_INPUT_TYPE)) {
					widget = new TextBox(formElement);
					break;
				}
				GWT.log("Skipping " + formElement + " because wrapping widget does not exist.", null);
				break;
			}

			if (null != widget) {
				final Hijacker hijacker = new Hijacker(widget.getElement());
				widget.removeFromParent();

				this.add(widget);

				hijacker.restore();

				final String formName = DOM.getElementProperty(formElement, "name");
				if (StringHelper.isNullOrEmpty(formName)) {
					GWT.log("The " + i + "th form element \"" + formElement
							+ "\"is missing a name attribute. This might result in it being omitted from any submitted form.", null);
				}
			}

		}
	}

	/**
	 * Submits the form.
	 * 
	 * <p>
	 * The FormPanel must <em>not</em> be detached (i.e. removed from its
	 * parent or otherwise disconnected from a {@link RootPanel}) until the
	 * submission is complete. Otherwise, notification of submission will fail.
	 * </p>
	 */
	public void submit() {
		// Fire the onSubmit event, because javascript's form.submit() does not
		// fire the built-in onsubmit event.
		if (false == this.getFormHandlers().fireOnSubmit(this)) {
			FormPanel.getSupport().submit(getElement(), this.getIFrame());
		}
	}

	protected void onAttach() {
		super.onAttach();

		// Create and attach a hidden iframe to the body element.
		final Element iframe = createIFrame();
		this.setIFrame(iframe);
		DOM.appendChild(RootPanel.getBodyElement(), iframe);

		// Hook up the underlying iframe's onLoad event when attached to the
		// DOM.
		// Making this connection only when attached avoids memory-leak issues.
		// The FormPanel cannot use the built-in GWT event-handling mechanism
		// because there is no standard onLoad event on iframes that works
		// across
		// browsers.
		FormPanel.getSupport().hookEvents(iframe, getElement(), new FormPanelImplHost() {

			public boolean onFormSubmit() {
				return FormPanel.this.onFormSubmit();
			}

			public void onFrameLoad() {
				FormPanel.this.onFrameLoad();
			}
		});
	}

	protected boolean onFormSubmit() {
		UncaughtExceptionHandler handler = GWT.getUncaughtExceptionHandler();
		if (handler != null) {
			return onFormSubmitAndCatch(handler);
		} else {
			return onFormSubmitImpl();
		}
	}

	private boolean onFormSubmitAndCatch(UncaughtExceptionHandler handler) {
		try {
			return onFormSubmitImpl();
		} catch (Throwable e) {
			handler.onUncaughtException(e);
			return false;
		}
	}

	private boolean onFormSubmitImpl() {
		// fireOnSubmit() returns true if the submit should be cancelled
		return !this.getFormHandlers().fireOnSubmit(this);
	}

	private void onFrameLoadAndCatch(final UncaughtExceptionHandler handler) {
		try {
			onFrameLoadImpl();
		} catch (Throwable e) {
			handler.onUncaughtException(e);
		}
	}

	private void onFrameLoadImpl() {
		// Fire onComplete events in a deferred command. This is necessary
		// because clients that detach the form panel when submission is
		// complete can cause some browsers (i.e. Mozilla) to go into an
		// 'infinite loading' state. See issue 916.
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				final Element iframe = FormPanel.this.getIFrame();
				FormPanel.this.getFormHandlers().fireOnComplete(FormPanel.this, FormPanel.getSupport().getContents(iframe));
			}
		});
	}

	protected void onFrameLoad() {
		final UncaughtExceptionHandler handler = GWT.getUncaughtExceptionHandler();
		if (handler != null) {
			onFrameLoadAndCatch(handler);
		} else {
			onFrameLoadImpl();
		}
	}

	protected void onDetach() {
		super.onDetach();

		final Element iframe = this.getIFrame();
		// Unhook the iframe's onLoad when detached.
		FormPanel.getSupport().unhookEvents(iframe, getElement());

		Dom.removeFromParent(iframe);
		this.clearIFrame();
	}

	protected void insert0(final Element element, final int indexBefore) {
		DOM.insertChild(this.getElement(), element, indexBefore);
	}

	protected void remove0(final Element element, final int index) {
		Dom.removeFromParent(element);
	}

	/**
	 * Gets the 'action' associated with this form. This is the URL to which it
	 * will be submitted.
	 * 
	 * @return the form's action
	 */
	public String getAction() {
		return DOM.getElementProperty(getElement(), "action");
	}

	/**
	 * Sets the 'action' associated with this form. This is the URL to which it
	 * will be submitted.
	 * 
	 * @param url
	 *            the form's action
	 */
	public void setAction(String url) {
		DOM.setElementProperty(getElement(), "action", url);
	}

	/**
	 * Gets the encoding used for submitting this form. This should be either
	 * {@link #ENCODING_MULTIPART} or {@link #ENCODING_URLENCODED}.
	 * 
	 * @return the form's encoding
	 */
	public String getEncoding() {
		return FormPanel.getSupport().getEncoding(getElement());
	}

	/**
	 * Sets the encoding used for submitting this form. This should be either
	 * {@link #ENCODING_MULTIPART} or {@link #ENCODING_URLENCODED}.
	 * 
	 * @param encodingType
	 *            the form's encoding
	 */
	public void setEncoding(String encodingType) {
		FormPanel.getSupport().setEncoding(getElement(), encodingType);
	}

	/**
	 * Gets the HTTP method used for submitting this form. This should be either
	 * {@link #METHOD_GET} or {@link #METHOD_POST}.
	 * 
	 * @return the form's method
	 */
	public String getMethod() {
		return DOM.getElementProperty(getElement(), "method");
	}

	/**
	 * Sets the HTTP method used for submitting this form. This should be either
	 * {@link #METHOD_GET} or {@link #METHOD_POST}.
	 * 
	 * @param method
	 *            the form's method
	 */
	public void setMethod(String method) {
		DOM.setElementProperty(getElement(), "method", method);
	}

	/**
	 * Gets the form's 'target'. This is the name of the {@link NamedFrame} that
	 * will receive the results of submission, or <code>null</code> if none
	 * has been specified.
	 * 
	 * @return the form's target.
	 */
	public String getTarget() {
		return DOM.getElementProperty(getElement(), "target");
	}

	public void setTarget(String target) {
		DOM.setElementProperty(getElement(), "target", target);
	}

	/**
	 * The hidden iframe that the form is submitted too.
	 */
	private Element iframe;

	protected Element getIFrame() {
		ObjectHelper.checkNotNull("field:iframe", iframe);
		return iframe;
	}

	protected void setIFrame(final Element iframe) {
		ObjectHelper.checkNotNull("parameter:iframe", iframe);
		this.iframe = iframe;
	}

	protected void clearIFrame() {
		this.iframe = null;
	}

	protected Element createIFrame() {
		// Attach a hidden IFrame to the form. This is the target iframe to
		// which
		// the form will be submitted. We have to create the iframe using
		// innerHTML,
		// because setting an iframe's 'name' property dynamically doesn't work
		// on
		// most browsers.
		final Element dummy = DOM.createDiv();
		final String frameName = this.getTarget();

		// FIXME
		// http://code.google.com/p/google-web-toolkit/issues/detail?id=1205
		DOM.setInnerHTML(dummy, "<iframe src=\"javascript='<html></html>'\" name=\"" + frameName
				+ "\" style=\"width:0;height:0;border:0\">");

		return DOM.getFirstChild(dummy);
	}

	private FormHandlerCollection formHandlers;

	protected FormHandlerCollection getFormHandlers() {
		ObjectHelper.checkNotNull("parameter:formHandlers", formHandlers);
		return this.formHandlers;
	}

	protected void setFormHandlers(final FormHandlerCollection formHandlers) {
		ObjectHelper.checkNotNull("parameter:formHandlers", formHandlers);
		this.formHandlers = formHandlers;
	}

	protected FormHandlerCollection createFormHandlers() {
		return new FormHandlerCollection();
	}

	public void addFormHandler(final FormHandler formHandler) {
		ObjectHelper.checkNotNull("parameter:formHandler", formHandler);
		this.getFormHandlers().add(formHandler);
	}

	public void removeFormHandler(final FormHandler formHandler) {
		ObjectHelper.checkNotNull("parameter:formHandler", formHandler);
		this.getFormHandlers().remove(formHandler);
	}
}
