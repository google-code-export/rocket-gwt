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
package rocket.widget.rebind.htmltemplatefactory;

import java.util.List;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.VirtualMethodVisitor;
import rocket.util.client.ObjectHelper;

/**
 * This generator creates a new factory that binds elements identified by id
 * with widget instances.
 * 
 * The developer needs to define an interface with one or more getters. Each
 * getter must have a subclass of widget as its return type. The method must
 * also declare no parameters. Using deferred binding this generator will create
 * the implementation.
 * 
 * @author Miroslav Pokorny
 */
public class HtmlTemplateGenerator extends Generator {

	protected NewConcreteType assembleNewType(final Type interfacee, final String newTypeName) {
		this.verifyImplementsHtmlTemplate(interfacee);

		final NewConcreteType newType = this.getGeneratorContext().newConcreteType();
		newType.setAbstract(false);
		newType.setFinal(true);
		newType.setName(newTypeName);
		newType.setSuperType(this.getHtmlTemplateFactoryImpl());
		newType.addInterface(interfacee);

		this.implementInterfaceMethods(interfacee, newType);

		return newType;
	}

	/**
	 * Verifies that the given interface type implements HtmlTemplateFactory
	 * throwing an exception if it doesnt.
	 * 
	 * @param interfacee
	 *            The type being tested.
	 */
	protected void verifyImplementsHtmlTemplate(final Type interfacee) {
		if (false == interfacee.isAssignableTo(this.getHtmlTemplateFactory())) {
			throwDoesntImplementHtmlTemplate(interfacee);
		}
	}

	protected void throwDoesntImplementHtmlTemplate(final Type interfacee) {
		throw new HtmlTemplateGeneratorException("The type " + interfacee + " is not an interface or doesnt implement "
				+ Constants.HTML_TEMPLATE_FACTORY);
	}

	/**
	 * Finds and implements all public methods
	 * 
	 * @param interface
	 *            The interface implemented by the new type
	 * @param newType
	 *            The new type being generated
	 */
	protected void implementInterfaceMethods(final Type interfacee, final NewConcreteType newType) {
		ObjectHelper.checkNotNull("parameter:interface", interfacee);
		ObjectHelper.checkNotNull("parameter:newType", newType);

		final VirtualMethodVisitor visitor = new VirtualMethodVisitor() {
			protected boolean visit(final Method method) {
				if (method.getVisibility() == Visibility.PUBLIC) {
					HtmlTemplateGenerator.this.implementMethod(method, newType);
				}
				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		visitor.start(interfacee);
	}

	/**
	 * Implements the method giving it a body depending on the method's return
	 * type.
	 * 
	 * @param method
	 *            The method being implemented
	 * @param newType
	 *            The recipient of all new methods.
	 */
	protected void implementMethod(final Method method, final NewConcreteType newType) {
		ObjectHelper.checkNotNull("parameter:method", method);
		ObjectHelper.checkNotNull("parameter:newType", newType);

		if (method.getParameters().size() != 0) {
			throwMethodHasParametersException(method);
		}

		final NewMethod newMethod = method.copy(newType);

		CodeBlock body = null;
		while (true) {
			final Type widgetType = method.getReturnType();

			if (widgetType.equals(this.getTextBox())) {
				body = buildTextBoxGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getPasswordTextBox())) {
				body = buildPasswordTextBoxGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getTextArea())) {
				body = buildTextAreaGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getRadioButton())) {
				body = buildRadioButtonGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getCheckBox())) {
				body = buildCheckBoxGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getListBox())) {
				body = buildListBoxGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getLabel())) {
				body = buildLabelGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getButton())) {
				body = buildButtonGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getImage())) {
				body = buildImageGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getHyperlink())) {
				body = buildHyperlinkGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getHtml())) {
				body = buildHtmlGetterMethodBody(method);
				break;
			}
			if (widgetType.equals(this.getFormPanel())) {
				body = buildFormPanelGetterMethodBody(method);
				break;
			}

			throwUnsupportedWidgetTypeException(method);
			break;
		}
		newMethod.setBody(body);
	}

	/**
	 * Builds a codeblock that can be used to bind a textbox to a input element
	 * of type after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildTextBoxGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.TEXTBOX_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind a password to a input element
	 * of type after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildPasswordTextBoxGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.PASSWORD_TEXTBOX_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind a textarea to a textarea
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildTextAreaGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.TEXTAREA_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind a list box to a select
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildListBoxGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.LISTBOX_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind a check box to an input
	 * element of type radio after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildCheckBoxGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.CHECKBOX_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind a radio button to a input
	 * element of type radio after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildRadioButtonGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.RADIO_BUTTON_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind a label (a div element) to an
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildLabelGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.LABEL_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind a button to a button element
	 * after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildButtonGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.BUTTON_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind an Image to an img element
	 * after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildImageGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.IMAGE_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind an Hyperlink to a anchor
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildHyperlinkGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.HYPERLINK_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind an FormPanel to a form
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildFormPanelGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.FORM_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	/**
	 * Builds a codeblock that can be used to bind a html (a div element) to an
	 * element after locating it by id.
	 * 
	 * @param method
	 *            The interface method being processed.
	 * @return The codeblock
	 */
	protected CodeBlock buildHtmlGetterMethodBody(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final String id = this.getId(method);

		return new GetterMethodBodyTemplatedFile() {
			protected String getTemplateFilename() {
				return Constants.HTML_TEMPLATE;
			}

			protected GeneratorContext getGeneratorContext() {
				return HtmlTemplateGenerator.this.getGeneratorContext();
			}

			protected String getId() {
				return id;
			}
		};
	}

	protected void throwMethodHasParametersException(final Method method) {
		throw new HtmlTemplateGeneratorException("HtmlTemplateFactory methods such as " + method + " must not have any parameters.");
	}

	protected void throwUnsupportedWidgetTypeException(final Method method) {
		throw new HtmlTemplateGeneratorException("The return type of the method " + method + " is not of a supported widget type.");
	}

	/**
	 * Helper which locates an id value from an annotation.
	 * 
	 * @param method
	 *            The interface method
	 * @return The id.
	 */
	protected String getId(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		final List values = method.getMetadataValues(Constants.ID_ANNOTATION);
		if (null == values || values.size() != 1) {
			throwUnableToFindIdAnnotationException(method);
		}
		return (String) values.get(0);
	}

	protected void throwUnableToFindIdAnnotationException(final Method method) {
		throw new HtmlTemplateGeneratorException("Unable to find an id annotation upon the method " + method);
	}

	protected Type getHtmlTemplateFactory() {
		return this.getGeneratorContext().getType(Constants.HTML_TEMPLATE_FACTORY);
	}

	protected Type getHtmlTemplateFactoryImpl() {
		return this.getGeneratorContext().getType(Constants.HTML_TEMPLATE_FACTORY_IMPL);
	}

	protected Type getTextBox() {
		return this.getGeneratorContext().getType(Constants.TEXTBOX);
	}

	protected Type getPasswordTextBox() {
		return this.getGeneratorContext().getType(Constants.PASSWORD_TEXTBOX);
	}

	protected Type getTextArea() {
		return this.getGeneratorContext().getType(Constants.TEXTAREA);
	}

	protected Type getRadioButton() {
		return this.getGeneratorContext().getType(Constants.RADIO_BUTTON);
	}

	protected Type getCheckBox() {
		return this.getGeneratorContext().getType(Constants.CHECKBOX);
	}

	protected Type getListBox() {
		return this.getGeneratorContext().getType(Constants.LISTBOX);
	}

	protected Type getLabel() {
		return this.getGeneratorContext().getType(Constants.LABEL);
	}

	protected Type getButton() {
		return this.getGeneratorContext().getType(Constants.BUTTON);
	}

	protected Type getImage() {
		return this.getGeneratorContext().getType(Constants.IMAGE);
	}

	protected Type getHyperlink() {
		return this.getGeneratorContext().getType(Constants.HYPERLINK);
	}

	protected Type getHtml() {
		return this.getGeneratorContext().getType(Constants.HTML);
	}

	protected Type getFormPanel() {
		return this.getGeneratorContext().getType(Constants.FORM_PANEL);
	}

	protected GeneratorContext createGeneratorContext() {
		return new GeneratorContext() {
			protected String getGeneratedTypeNameSuffix() {
				return Constants.SUFFIX;
			}
		};
	}
}
