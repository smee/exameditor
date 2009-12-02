package de.elateportal.editor.components.panels.tasks.mc;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.MinimumValidator;

import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.TextSubTaskDef;
import de.elateportal.model.McSubTaskDef.Correct;
import de.elateportal.model.McSubTaskDef.Incorrect;

public class McSubtaskDefInputPanel extends Panel {
	  private final MCAnswersInputPanel correctAnswers,incorrectAnswers;
	
    public McSubtaskDefInputPanel(final String id, final IModel<McSubTaskDef> model) {
        super(id, model);
        final TextField<Integer> numanswersInput = new TextField<Integer>("displayedAnswers");
        numanswersInput.add(new MinimumValidator<Integer>(1));
        add(numanswersInput);
        // TODO add validator (min<=max, min >0, max<=#answers)
        add(new TextField<TextSubTaskDef>("maxCorrectAnswers"));
        add(new TextField<TextSubTaskDef>("minCorrectAnswers"));
        add(new AjaxCheckBox("preserveOrderOfAnswers") {

					@Override
          protected void onUpdate(AjaxRequestTarget target) {
	          correctAnswers.setMoveButtonsVisible(getModelObject());
	          incorrectAnswers.setMoveButtonsVisible(getModelObject());
	          target.addComponent(correctAnswers);
	          target.addComponent(incorrectAnswers);
          }
        	
        });
        boolean showMoveButtons = model.getObject().isPreserveOrderOfAnswers();
				correctAnswers=new MCAnswersInputPanel("correctanswers", Correct.class, new PropertyModel<Correct>(model, "correct"),showMoveButtons);
        incorrectAnswers=new MCAnswersInputPanel("incorrectanswers", Incorrect.class, new PropertyModel<Incorrect>(model, "incorrect"),showMoveButtons);
        add(correctAnswers.setOutputMarkupId(true));
        add(incorrectAnswers.setOutputMarkupId(true));
    }
}
