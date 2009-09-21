package de.elateportal.editor.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

import de.elateportal.editor.components.panels.tasks.SubtaskDefInputPanel;
import de.elateportal.model.SubTaskDefType;

/**
 * @author sdienst
 */
public class EditSubtaskPage extends OverviewPage {

    public EditSubtaskPage(final Class<? extends SubTaskDefType> clazz) {
        add(new Label("heading", "Aufgabe bearbeiten"));
        add(createInputPanelFor("input", clazz));
    }

    private Component createInputPanelFor(final String id, final Class<? extends SubTaskDefType> clazz) {

        return new SubtaskDefInputPanel(id, clazz);
    }
}
