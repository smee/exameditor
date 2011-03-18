package de.elatexam.editor.components.panels.tasks;

import net.databinder.components.hib.DataForm;
import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.MinimumValidator;

import de.elatexam.editor.components.panels.tasks.taskblockspecifics.TaskblockSpecificConfigPanel;
import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.ClozeTaskBlock.ClozeConfig;
import de.elatexam.model.McTaskBlock;
import de.elatexam.model.TaskBlock;
import de.elatexam.model.McTaskBlock.McConfig;

/**
 * @author sdienst
 */
public class TaskBlockConfigPanel extends Panel {
    private class TaskBlockConfigForm extends DataForm<TaskBlock> {

    private FeedbackPanel feedback;


    public TaskBlockConfigForm(final String id, final HibernateObjectModel<TaskBlock> model) {
      super(id, model);
    }
/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
		protected void onInitialize() {
			super.onInitialize();
			add(feedback = new FeedbackPanel("feedback"));
			feedback.setOutputMarkupId(true);
			add(new TextField<Float>("config.pointsPerTask"));
			add(new TextField<Integer>("config.noOfSelectedTasks").add(new MinimumValidator<Integer>(0)));
			add(new CheckBox("config.preserveOrder"));
			add(new TaskblockSpecificConfigPanel("specific", getModel()));
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


  public TaskBlockConfigPanel(final String id, final HibernateObjectModel<TaskBlock> model) {
    super(id, model);
    add(new TaskBlockConfigForm("categoryform", model));
  }

}
