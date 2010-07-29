package de.elatexam.editor.pages.taskdef;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.google.common.collect.ImmutableMap;

import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.MappingTaskBlock;
import de.elatexam.model.McTaskBlock;
import de.elatexam.model.PaintTaskBlock;
import de.elatexam.model.TaskBlock;
import de.elatexam.model.TextTaskBlock;

/**
 * @author Steffen Dienst
 *
 */
public abstract class TaskBlockSelectionPanel<T extends Class<? extends TaskBlock>> extends Panel {
    @SuppressWarnings("unchecked")
    final List<T> taskblocktypes = (List<T>) Arrays.asList(
            McTaskBlock.class,
            MappingTaskBlock.class,
            ClozeTaskBlock.class,
            TextTaskBlock.class,
            PaintTaskBlock.class);

    static final Map<Class<? extends TaskBlock>, String> labels = ImmutableMap.of(
            McTaskBlock.class, "Multiple Choice",
            MappingTaskBlock.class, "Zuordnung",
            ClozeTaskBlock.class, "Lückentext",
            TextTaskBlock.class, "Freitext",
            PaintTaskBlock.class, "Zeichnen");


    public TaskBlockSelectionPanel(String id, final ModalWindow modalwindow) {
        super(id);

        Form<T> form = new Form<T>("form");
        add(form);
        final RadioChoice<T> choice = new RadioChoice<T>("blockselection",
                new Model<T>(),
                taskblocktypes,
                new IChoiceRenderer<T>() {

                    @Override
                    public Object getDisplayValue(T taskblockClass) {
                        return labels.get(taskblockClass);
                    }

                    @Override
                    public String getIdValue(T taskblockClass, int index) {
                        return Integer.toString(index);
                    }

                });

        form.add(choice);
        form.add(new Button("close") {

            @Override
            public void onSubmit() {
                onSelect(choice.getModelObject());
                // modalwindow.close(target);
            }
        });
    }

    abstract protected void onSelect(T taskblockclass);

}