package de.elateportal.editor.components.panels.tasks;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

import de.elateportal.model.TextSubTaskDef;

public class ClozeSubtaskDefInputPanel extends Panel {

    public ClozeSubtaskDefInputPanel(final String id) {
        super(id);
        add(new TextArea<TextSubTaskDef>("initialTextFieldValue"));
        add(new TextField<TextSubTaskDef>("textFieldHeight"));
        add(new TextField<TextSubTaskDef>("textFieldWidth"));
    }
}
