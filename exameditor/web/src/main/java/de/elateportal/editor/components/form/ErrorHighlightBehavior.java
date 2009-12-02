package de.elateportal.editor.components.form;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.Model;

/**
 * Add css class "error" to every form component, that has a validation error.
 * @author Steffen Dienst
 *
 */
public class ErrorHighlightBehavior extends AttributeAppender {
	private static final long serialVersionUID = 1L;

	public ErrorHighlightBehavior() {
		super("class", true, Model.of("error"), " ");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.AttributeModifier#isEnabled(org.apache.wicket.Component)
	 */
	@Override
	public boolean isEnabled(Component component) {
		return !((FormComponent) component).isValid();
	}
}
