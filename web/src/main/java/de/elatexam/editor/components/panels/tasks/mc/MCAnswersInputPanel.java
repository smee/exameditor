package de.elatexam.editor.components.panels.tasks.mc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

import de.elatexam.editor.components.listeditor.EditorButton;
import de.elatexam.editor.components.listeditor.ListEditor;
import de.elatexam.editor.components.listeditor.ListItem;
import de.elatexam.editor.components.listeditor.MoveDownButton;
import de.elatexam.editor.components.listeditor.MoveUpButton;
import de.elatexam.editor.components.listeditor.RemoveButton;
import de.elatexam.editor.components.panels.tasks.SubtaskSpecificsInputPanel;
import de.elatexam.model.McSubTaskDef.McSubTaskDefAnswerDefinitionsItem;
import de.elatexam.model.NamedString;
/**
 * @author sdienst
 */
public class MCAnswersInputPanel extends SubtaskSpecificsInputPanel<List<McSubTaskDefAnswerDefinitionsItem>> {

	private final WebMarkupContainer container;
	Collection<EditorButton> moveButtons = new ArrayList<EditorButton>();

	/**
	 * @param id
	 * @param modelElementClass
	 * @param model
	 * @param moveable
	 */
	@SuppressWarnings("unchecked")
    public MCAnswersInputPanel(final String id, final IModel<List<McSubTaskDefAnswerDefinitionsItem>> model,
            final boolean moveable) {
		super(id, model);

        add(new Label("title", "Anworten"));

		container = new WebMarkupContainer("answerrepeater");
        final ListEditor<McSubTaskDefAnswerDefinitionsItem> answers = new ListEditor<McSubTaskDefAnswerDefinitionsItem>("mcanswer", model) {

			@Override
            protected void onPopulateItem(final ListItem<McSubTaskDefAnswerDefinitionsItem> item) {
				// item.add(new TextField("id"));
                McAnswersCorrectnessModel answerCorrectnessModel = new McAnswersCorrectnessModel(item.getModel());
                item.add(new AjaxCheckBox("correct", answerCorrectnessModel) {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        // send the textfield value via ajax to the server, fixes the bug,
                        // that entered value gets lost when using the up/down buttons
                    }
                });

                item.add(new TextField<String>("value", new McAnswersModel(item.getModel())).add(new AjaxFormComponentUpdatingBehavior(
				    "onblur") {
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						// send the textfield value via ajax to the server, fixes the bug,
						// that entered value gets lost when using the up/down buttons
					}

				}));
				// links, images for changing order and removing answers
				MoveUpButton mub = new MoveUpButton("moveUp");
				MoveDownButton mdb = new MoveDownButton("moveDown");
				moveButtons.add(mub);
				moveButtons.add(mdb);
				item.add(mub.setVisibilityAllowed(moveable));
				item.add(mdb.setVisibilityAllowed(moveable));
				item.add(new RemoveButton("delete"));
			}

			private void setId(Object o, Long id) {
                ((McSubTaskDefAnswerDefinitionsItem) o).setHjid(id);
			}

			@Override
			public void updateModel() {
				/*
				 * XXX extremely ugly hack: jpa doesn't handle ordered lists, so we need
				 * to sort the primary keys of our element list. This way we will get
				 * the list in that very order upon the next retrieval.
				 */
				// find all primary keys
				final List<Long> primaryKeys = new ArrayList<Long>();
				for (final Object o : items) {
                    Long id = ((McSubTaskDefAnswerDefinitionsItem) o).getHjid();
					if (id != null) {
                        primaryKeys.add(id);
                    }
				}
				// make sure they are in an ascending order
				Collections.sort(primaryKeys);
				// set the primary keys in the right order
				for (int i = 0; i < primaryKeys.size(); i++) {
					setId(items.get(i), primaryKeys.get(i));
				}
				for (int i = primaryKeys.size(); i < items.size(); i++) {
					setId(items.get(i), null);
				}
				super.updateModel();
			}
		};
		container.setOutputMarkupId(true);
		container.add(answers);
		add(container);

        final AjaxFallbackLink addAnswer = new AjaxFallbackLink("addanswer") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                NamedString answer = new NamedString();
                answer.setId("" + System.nanoTime());
                McSubTaskDefAnswerDefinitionsItem answerDef = new McSubTaskDefAnswerDefinitionsItem();
                answerDef.setItemIncorrect(answer);
                answers.addItem(answerDef);

                if (target != null) {
                    target.addComponent(container);
                }
            }
		};
		add(addAnswer);
	}

	/**
	 * Toggle visibility of moveup/down buttons.
	 *
	 * @param flag
	 */
	public void setMoveButtonsVisible(boolean flag) {
		for (EditorButton b : moveButtons) {
            b.setVisibilityAllowed(flag);
        }
	}
}
