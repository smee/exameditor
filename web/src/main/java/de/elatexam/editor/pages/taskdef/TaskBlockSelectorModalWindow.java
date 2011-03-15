package de.elatexam.editor.pages.taskdef;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TaskBlock;
import de.elatexam.model.manual.HomogeneousTaskBlock;

/**
 * @author Steffen Dienst
 *
 */
public abstract class TaskBlockSelectorModalWindow extends ModalWindow {

    public TaskBlockSelectorModalWindow(String id) {
        super(id);
        setTitle("Bitte wählen Sie den Aufgabentyp");
        setInitialWidth(350);
        setInitialHeight(250);
        setContent(new TaskBlockSelectionPanel(getContentId(), this) {

            @Override
            protected void onSelect(Class<? extends HomogeneousTaskBlock> taskblockclass) {
                TaskBlockSelectorModalWindow.this.onSelect(taskblockclass);
            }

        });

    }

    abstract void onSelect(Class<? extends HomogeneousTaskBlock> taskblockclass);
}