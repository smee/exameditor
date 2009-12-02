package de.elateportal.editor.components.form;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.markup.html.form.FormComponent.IVisitor;


/**
 * @author Steffen Dienst
 *
 */
public class ShinyFormVisitor implements IVisitor, Serializable {

	private final Set<IFormVisitorParticipant> visited = new HashSet<IFormVisitorParticipant>();


	public Object formComponent(IFormVisitorParticipant formComponent) {
		if (!visited.contains(formComponent)) {
			visited.add(formComponent);

			((FormComponent) formComponent).add(new ErrorHighlightBehavior());
		}
		return Component.IVisitor.CONTINUE_TRAVERSAL;
	}

}
