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
import de.elatexam.model.manual.HomogeneousTaskBlock;

/**
 * @author Steffen Dienst
 *
 */
public abstract class TaskBlockSelectionPanel extends Panel {
    @SuppressWarnings("unchecked")
    final List<Class<? extends HomogeneousTaskBlock>> taskblocktypes = Arrays.asList(
            McTaskBlock.class,
            MappingTaskBlock.class,
            ClozeTaskBlock.class,
            TextTaskBlock.class,
            PaintTaskBlock.class);

    static final ImmutableMap<Class<? extends HomogeneousTaskBlock>, String> labels = ImmutableMap.of(
            McTaskBlock.class, "Multiple Choice",
            MappingTaskBlock.class, "Zuordnung",
            ClozeTaskBlock.class, "Lückentext",
            TextTaskBlock.class, "Freitext",
            PaintTaskBlock.class, "Zeichnen");


    public TaskBlockSelectionPanel(String id, final ModalWindow modalwindow) {
        super(id);

        Form<Class<? extends HomogeneousTaskBlock>> form = new Form<Class<? extends HomogeneousTaskBlock>>("form");
        add(form);
        final RadioChoice<Class<? extends HomogeneousTaskBlock>> choice = new RadioChoice<Class<? extends HomogeneousTaskBlock>>("blockselection",
                new Model<Class<? extends HomogeneousTaskBlock>>(),
                taskblocktypes,
                new IChoiceRenderer<Class<? extends HomogeneousTaskBlock>>() {

                    @Override
                    public Object getDisplayValue(Class<? extends HomogeneousTaskBlock> taskblockClass) {
                        return labels.get(taskblockClass);
                    }

                    @Override
                    public String getIdValue(Class<? extends HomogeneousTaskBlock> taskblockClass, int index) {
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

    abstract protected void onSelect(Class<? extends HomogeneousTaskBlock> taskblockclass);

}