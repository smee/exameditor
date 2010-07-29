package de.elatexam.editor.pages.taskdef;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

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
            protected void onSelect(Class taskblockclass) {
                TaskBlockSelectorModalWindow.this.onSelect(taskblockclass);
            }

        });

    }

    abstract void onSelect(Class taskblockclass);
}