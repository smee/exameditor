package de.elateportal.editor.components.panels.tasks;

import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import de.elateportal.editor.components.form.ShinyForm;
import de.elateportal.model.Category;

/**
 * @author sdienst
 */
public class CategoryPanel extends Panel {
	private class CategoryForm extends ShinyForm<Category> {

		private FeedbackPanel feedback;

		public CategoryForm(final String id) {
			super(id, Category.class);
			init();
		}

		public CategoryForm(final String id, final HibernateObjectModel<Category> hibernateObjectModel) {
			super(id, hibernateObjectModel);
			init();
		}

		private void init() {
			add(feedback = new FeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);
			add(new TextField<String>("id").setRequired(true));
			add(new TextField<String>("title").setRequired(true));
			add(new TextField<Integer>("tasksPerPage"));

			add(new AjaxButton("saveButton") {

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
					target.addComponent(feedback);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					feedback.info("Gespeichert!");
					target.addComponent(feedback);
				}

			});
			add(new org.apache.wicket.markup.html.form.Button("cancelButton") {
				@Override
				public void onSubmit() {
					clearPersistentObject();
				}
			}.setDefaultFormProcessing(false));
		}

	}

	public CategoryPanel(final String id) {
		super(id);
		add(new CategoryForm(id));
	}

	public CategoryPanel(final String id, final HibernateObjectModel<Category> model) {
		super(id, model);
		add(new CategoryForm("categoryform", model));
	}

}
