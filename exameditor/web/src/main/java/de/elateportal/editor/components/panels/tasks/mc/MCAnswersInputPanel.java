package de.elateportal.editor.components.panels.tasks.mc;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import de.elateportal.editor.components.listeditor.ListEditor;
import de.elateportal.editor.components.listeditor.ListItem;
import de.elateportal.editor.components.listeditor.MoveDownButton;
import de.elateportal.editor.components.listeditor.MoveUpButton;
import de.elateportal.editor.components.listeditor.RemoveButton;
import de.elateportal.model.McSubTaskDef.Correct;
import de.elateportal.model.McSubTaskDef.Incorrect;

/**
 * @author sdienst
 */
public class MCAnswersInputPanel extends Panel {

    private final WebMarkupContainer container;

    @SuppressWarnings("unchecked")
    public MCAnswersInputPanel(final String id, final Class<?> modelElementClass, final IModel model) {
        super(id, model);

        String title = "Richtige Anworten";
        if (!modelElementClass.isAssignableFrom(modelElementClass)) {
            title = "Falsche Antworten";
        }

        add(new Label("title", title));

        container = new WebMarkupContainer("answerrepeater");
        final ListEditor answers = new ListEditor("mcanswer", model) {
            @Override
            protected void onPopulateItem(final ListItem item) {
                // item.add(new TextField("id"));
                item.add(new TextField("value", new PropertyModel(item.getModel().getObject(), "value")));
                // links, images for changing order and removing answers
                item.add(new MoveUpButton("moveUp"));
                item.add(new MoveDownButton("moveDown"));
                item.add(new RemoveButton("delete"));
            }
        };
        container.setOutputMarkupId(true);
        container.add(answers);
        add(container);

        final AjaxButton addAnswer = new AjaxButton("addanswer") {
            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form<?> form) {

                if (modelElementClass.equals(Correct.class)) {
                    final Correct answer = new Correct();
                    answer.setId("" + System.nanoTime());
                    answers.addItem(answer);
                } else {
                    final Incorrect answer = new Incorrect();
                    answer.setId("" + System.nanoTime());
                    answers.addItem(answer);
                }

                if (target != null) {
                    target.addComponent(container);
                }
            }
        };
        addAnswer.setDefaultFormProcessing(false);
        add(addAnswer);
    }
}
