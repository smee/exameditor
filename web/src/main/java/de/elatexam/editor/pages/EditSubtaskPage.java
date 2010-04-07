package de.elatexam.editor.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

import de.elatexam.model.SubTaskDefType;
import de.elatexam.editor.components.panels.tasks.SubtaskDefInputPanel;

/**
 * @author sdienst
 */
public class EditSubtaskPage<T extends SubTaskDefType> extends SecurePage {

	public EditSubtaskPage(final Class<T> clazz) {
		this(clazz, null);
	}

	public EditSubtaskPage(final Class<T> clazz, final T subTaskDefType) {
		add(new Label("heading", "Aufgabe bearbeiten"));
		add(createInputPanelFor("input", clazz, subTaskDefType));

	}

	private Component createInputPanelFor(final String id, final Class<T> clazz, final T subTaskDefType) {

		return new SubtaskDefInputPanel(id, clazz, subTaskDefType);
	}
}
