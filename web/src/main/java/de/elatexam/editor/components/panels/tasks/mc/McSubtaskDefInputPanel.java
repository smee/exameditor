package de.elatexam.editor.components.panels.tasks.mc;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.MinimumValidator;

import de.elatexam.editor.components.panels.tasks.SubtaskSpecificsInputPanel;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.McSubTaskDef.McSubTaskDefAnswerDefinitionsItem;
import de.elatexam.model.McSubTaskDef.Type;

/**
 * @author Steffen Dienst
 *
 */
public class McSubtaskDefInputPanel extends SubtaskSpecificsInputPanel<McSubTaskDef> implements IFormModelUpdateListener{
    private final MCAnswersInputPanel mcAnswers;

	public McSubtaskDefInputPanel(final String id, final IModel<McSubTaskDef> model) {
		super(id, model);
		boolean showMoveButtons = model.getObject().isPreserveOrderOfAnswers();

        mcAnswers = new MCAnswersInputPanel("answers", new PropertyModel<List<McSubTaskDef.McSubTaskDefAnswerDefinitionsItem>>(model, "answerDefinitionsItems"), model.getObject().getCategory(),showMoveButtons);
		add(mcAnswers.setOutputMarkupId(true));

		add(new AjaxCheckBox("preserveOrderOfAnswers") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				mcAnswers.setMoveButtonsVisible(getModelObject());
				target.addComponent(mcAnswers);
			}

		});
		final TextField<Integer> numanswersInput = new TextField<Integer>("displayedAnswers");
		numanswersInput.add(new MinimumValidator<Integer>(1));
		add(numanswersInput);
		// TODO add validator (min<=max, min >0, max<=#answers)
		add(new TextField<Integer>("minCorrectAnswers").add(new MinimumValidator<Integer>(1)));
		add(new TextField<Integer>("maxCorrectAnswers"));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IFormModelUpdateListener#updateModel()
	 */
	@Override
	public void updateModel() {
		// XXX make the mc a single choice taskdef iff there is just one correct answer 
		McSubTaskDef mc = (McSubTaskDef) getDefaultModelObject();
		int correct=0;
		for(McSubTaskDefAnswerDefinitionsItem answer:mc.getAnswerDefinitionsItems())
			if(answer.getItemCorrect()!=null)
				correct++;
		if(correct==1)
			mc.setCategory(Type.SINGLE_SELECT);
		else
			mc.setCategory(Type.MULTIPLE_SELECT);
		
	}
	
}
