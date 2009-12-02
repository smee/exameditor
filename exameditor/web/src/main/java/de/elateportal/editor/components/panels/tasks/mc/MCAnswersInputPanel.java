package de.elateportal.editor.components.panels.tasks.mc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import de.elateportal.editor.components.listeditor.EditorButton;
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
    Collection<EditorButton> moveButtons=new ArrayList<EditorButton>();
    /**
     * @param id
     * @param modelElementClass
     * @param model
     */
    @SuppressWarnings("unchecked")
    public MCAnswersInputPanel(final String id, final Class<?> modelElementClass, final IModel model) {
        this(id, modelElementClass, model, true);
    }

    /**
     * @param id
     * @param modelElementClass
     * @param model
     * @param moveable
     */
    @SuppressWarnings("unchecked")
    public MCAnswersInputPanel(final String id, final Class<?> modelElementClass, final IModel model, final boolean moveable) {
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
                item.add(new TextField("value", new PropertyModel(item.getModel(), "value")));
                // links, images for changing order and removing answers
                MoveUpButton mub=new MoveUpButton("moveUp");
                MoveDownButton mdb=new MoveDownButton("moveDown");
                moveButtons.add(mub);
                moveButtons.add(mdb);
                item.add(mub.setVisibilityAllowed(moveable));
                item.add(mdb.setVisibilityAllowed(moveable));
                item.add(new RemoveButton("delete"));
            }

            @Override
            public void updateModel() {
                /*
                 * XXX extremely ugly hack: jpa doesn't handle ordered lists, so we need to sort the primary keys of our
                 * element list. This way we will get the list in that very order upon the next retrieval.
                 */
                // find all primary keys
                final List<Long> primaryKeys = new ArrayList<Long>();
                for (final Object o : items) {
                	Long id = getId(o);
                    if(id!=null)
                    	primaryKeys.add(id);
                    
                }
                // make sure they are in an ascending order
                Collections.sort(primaryKeys);
                // set the primary keys in the right order
                for (int i = 0; i < primaryKeys.size(); i++) {
                    setId(items.get(i),primaryKeys.get(i));
                }
                for (int i = primaryKeys.size();i<items.size(); i++) {
                	setId(items.get(i),null);
                }
                super.updateModel();
            }

						private void setId(Object o, Long id) {
							if (o instanceof Correct) {
								((Correct) o).setHjid(id);
							} else {
								((Incorrect) o).setHjid(id);
							}
						}
						private Long getId(Object o) {
							if (o instanceof Correct) {
								return ((Correct) o).getHjid();
							} else {
								return ((Incorrect) o).getHjid();
							}
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
    /**
     * Toggle visibility of moveup/down buttons.
     * @param flag
     */
    public void setMoveButtonsVisible(boolean flag) {
    	for(EditorButton b:moveButtons)
    		b.setVisibilityAllowed(flag);
    }
}
