package de.elatexam.editor.components.panels.tasks;

import java.util.Arrays;

import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.validator.MinimumValidator;

import wicket.contrib.tinymce.TinyMceBehavior;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.ComplexTaskDef.Config.CorrectionMode.CorrectOnlyProcessedTasks;
import de.elatexam.model.ComplexTaskDef.Config.CorrectionMode.MultipleCorrectors;
import de.elatexam.model.ComplexTaskDef.Config.CorrectionMode.Regular;
import de.elatexam.editor.components.form.ShinyForm;

/**
 * @author sdienst
 */
public class ComplexTaskdefPanel extends Panel {
  private class ComplexTaskDefForm extends ShinyForm<ComplexTaskDef> {

    private FeedbackPanel feedback;

    public ComplexTaskDefForm(final String id) {
      super(id, ComplexTaskDef.class);
      init();
    }

    public ComplexTaskDefForm(final String id, final HibernateObjectModel<ComplexTaskDef> hibernateObjectModel) {
      super(id, hibernateObjectModel);
      init();
    }

    private void init() {
      add(feedback = new FeedbackPanel("feedback"));
      feedback.setOutputMarkupId(true);
      // add(new TextField<String>("id"));
      add(new TextField<String>("title").setRequired(true));
      add(new TextArea<String>("description").setRequired(true).add(new TinyMceBehavior(SubtaskDefInputPanel.createFullFeatureset())));
      add(new TextArea<String>("startText"));
      add(new CheckBox("showHandlingHintsBeforeStart"));

      add(new TextField<Integer>("config.time"));
      add(new TextField<Integer>("config.tries"));
      add(new TextField<Integer>("config.tasksPerPage").add(new MinimumValidator<Integer>(1)));
      add(new TextField<Integer>("config.kindnessExtensionTime"));
      // TODO add correction mode
      add(new DropDownChoice("config.correctionMode", Arrays.asList(Regular.class.getSimpleName(), CorrectOnlyProcessedTasks.class.getSimpleName(), MultipleCorrectors.class.getSimpleName())) {
        @Override
        protected boolean wantOnSelectionChangedNotifications() {
          return true;
        }

        @Override
        protected void onSelectionChanged(final Object newSelection) {
          System.out.println("selected " + newSelection);
        }
      });
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

  public ComplexTaskdefPanel(final String id) {
    super(id);
    add(new ComplexTaskDefForm(id));
  }

  public ComplexTaskdefPanel(final String id, final HibernateObjectModel<ComplexTaskDef> model) {
    super(id, model);
    add(new ComplexTaskDefForm("complexTaskDefForm", model));
  }

}
