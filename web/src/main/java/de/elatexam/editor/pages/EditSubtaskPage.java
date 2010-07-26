package de.elatexam.editor.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

import de.elatexam.editor.components.panels.tasks.SubtaskDefInputPanel;
import de.elatexam.model.SubTaskDef;

/**
 * @author sdienst
 */
public class EditSubtaskPage<T extends SubTaskDef> extends SecurePage {

	public EditSubtaskPage(final Class<T> clazz) {
		this(clazz, null);
	}

	public EditSubtaskPage(final Class<T> clazz, final T SubTaskDef) {
		add(new Label("heading", "Aufgabe bearbeiten"));
		add(createInputPanelFor("input", clazz, SubTaskDef));

	}

	private Component createInputPanelFor(final String id, final Class<T> clazz, final T SubTaskDef) {

		return new SubtaskDefInputPanel(id, clazz, SubTaskDef);
	}
}
