package de.elateportal.editor.components.panels.tasks;

import java.util.List;

import net.databinder.components.MoveDownButton;
import net.databinder.components.MoveUpButton;
import net.databinder.components.RemoveItemButton;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

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
        final ListView answers = new PropertyListView("mcanswer", model) {
            @Override
            protected void populateItem(final ListItem item) {
                // item.add(new TextField("id"));
                item.add(new TextField("value"));
                // links, images for changing order and removing answers
                item.add(new MoveUpButton("moveUp", item));
                item.add(new MoveDownButton("moveDown", item));
                item.add(new RemoveItemButton("delete", item));
            }
        }.setReuseItems(true);
        container.setOutputMarkupId(true);
        container.add(answers);
        add(container);

        add(new AjaxFallbackLink("addanswer", Model.of("Neue Aufgabe")) {
            @Override
            public void onClick(final AjaxRequestTarget target) {
                final List list = (List) MCAnswersInputPanel.this.getDefaultModelObject();
                if (modelElementClass.equals(Correct.class)) {
                    final Correct answer = new Correct();
                    answer.setId("" + System.nanoTime());
                    final boolean res = list.add(answer);
                    System.out.println(res);
                } else {
                    final Incorrect answer = new Incorrect();
                    answer.setId("" + System.nanoTime());
                    list.add(answer);
                }
                if (target != null) {
                    target.addComponent(container);
                }
                System.out.println(list.size());
            }
        });
    }
}
