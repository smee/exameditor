package de.elateportal.editor.components.panels.tasks.text;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

import de.elateportal.model.TextSubTaskDef;

public class TextSubtaskDefInputPanel extends Panel {

    public TextSubtaskDefInputPanel(final String id) {
        super(id);
        add(new TextArea<TextSubTaskDef>("initialTextFieldValue"));
        add(new TextField<TextSubTaskDef>("textFieldHeight"));
        add(new TextField<TextSubTaskDef>("textFieldWidth"));
    }
}
