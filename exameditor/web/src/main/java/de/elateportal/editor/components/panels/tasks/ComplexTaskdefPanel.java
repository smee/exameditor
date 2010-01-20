package de.elateportal.editor.components.panels.tasks;

import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.validator.MinimumValidator;

import wicket.contrib.tinymce.TinyMceBehavior;
import de.elateportal.editor.components.form.ShinyForm;
import de.elateportal.model.ComplexTaskDef;

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
