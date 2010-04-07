package de.elatexam.editor.components.form;

import net.databinder.components.hib.DataForm;
import net.databinder.models.hib.HibernateObjectModel;

/**
 * {@link DataForm} with red border on invalid form fields.
 * 
 * @author Steffen Dienst
 * 
 * @param <T>
 */
public class ShinyForm<T> extends DataForm<T> {
	private static final long serialVersionUID = 1L;

	private final ShinyFormVisitor shinyVisitor = new ShinyFormVisitor();

	public ShinyForm(String id) {
		super(id);
	}

	public ShinyForm(String id, Class<T> clazz) {
		super(id, clazz);
	}

	public ShinyForm(String id, HibernateObjectModel<T> hibernateObjectModel) {
		super(id, hibernateObjectModel);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		// attach behaviour to all form fields
		visitFormComponents(shinyVisitor);
	}
}
