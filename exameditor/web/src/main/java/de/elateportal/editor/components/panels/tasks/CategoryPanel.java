package de.elateportal.editor.components.panels.tasks;

import net.databinder.models.hib.HibernateObjectModel;

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

    public CategoryForm(final String id) {
      super(id, Category.class);
      init();
    }

    private void init() {
      add(new FeedbackPanel("feedback"));
      add(new TextField<String>("id").setRequired(true));
      add(new TextField<String>("title").setRequired(true));
      add(new TextField<Integer>("tasksPerPage"));

      add(new org.apache.wicket.markup.html.form.Button("saveButton"));
      add(new org.apache.wicket.markup.html.form.Button("cancelButton") {
        @Override
        public void onSubmit() {
          clearPersistentObject();
          // setResponsePage(returnPage);
        }
      }.setDefaultFormProcessing(false));
    }

    public CategoryForm(final String id, final HibernateObjectModel<Category> hibernateObjectModel) {
      super(id, hibernateObjectModel);
      init();
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

