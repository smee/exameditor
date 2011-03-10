package de.elatexam.editor.pages;

import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

import de.elatexam.editor.components.panels.tasks.SubtaskDefInputPanel;
import de.elatexam.model.SubTaskDef;

/**
 * @author sdienst
 */
public class EditSubtaskPage<T extends SubTaskDef> extends SecurePage {

	public EditSubtaskPage(final Class<T> clazz) {
        this(clazz, new HibernateObjectModel<T>(clazz));
	}

    public EditSubtaskPage(Class<T> clazz, HibernateObjectModel<T> model) {
        add(new Label("heading", "Aufgabe bearbeiten"));
        add(createInputPanelFor("input", clazz, model));
    }

    private Component createInputPanelFor(final String id, final Class<T> clazz, final HibernateObjectModel<T> model) {
        return new SubtaskDefInputPanel(id, model);
	}
}
