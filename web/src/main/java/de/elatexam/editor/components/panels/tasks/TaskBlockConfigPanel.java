package de.elatexam.editor.components.panels.tasks;

import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.validator.NumberValidator.MinimumValidator;

import de.elatexam.editor.components.form.ShinyForm;
import de.elatexam.model.TaskblockConfig;

/**
 * @author sdienst
 */
public class TaskBlockConfigPanel extends Panel {
  private class TaskBlockConfigForm extends ShinyForm<TaskblockConfig> {

    private FeedbackPanel feedback;

    public TaskBlockConfigForm(final String id) {
      super(id, TaskblockConfig.class);
      init();
    }

    public TaskBlockConfigForm(final String id, final HibernateObjectModel<TaskblockConfig> hibernateObjectModel) {
      super(id, hibernateObjectModel);
      init();
    }

    private void init() {
      add(feedback = new FeedbackPanel("feedback"));
      feedback.setOutputMarkupId(true);
      add(new TextField<Float>("pointsPerTask"));
      add(new TextField<Integer>("noOfSelectedTasks").add(MinimumValidator.minimum(0)));
      add(new CheckBox("preserveOrder"));

      add(new Button("saveButton") {

        @Override
        public void onSubmit() {
          super.onSubmit();
          info("Gespeichert!");
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

  public TaskBlockConfigPanel(final String id) {
    super(id);
    add(new TaskBlockConfigForm(id));
  }

  public TaskBlockConfigPanel(final String id, final HibernateObjectModel<TaskblockConfig> model) {
    super(id, model);
    add(new TaskBlockConfigForm("categoryform", model));
  }

}
