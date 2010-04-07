package de.elatexam.editor.components.panels.tasks.text;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;

import de.elatexam.model.TextSubTaskDef;
import de.elatexam.editor.components.panels.tasks.SubtaskSpecificsInputPanel;

public class TextSubtaskDefInputPanel extends SubtaskSpecificsInputPanel<TextSubTaskDef> {

    public TextSubtaskDefInputPanel(final String id) {
        super(id);
        add(new TextArea<TextSubTaskDef>("initialTextFieldValue"));
        add(new TextField<TextSubTaskDef>("textFieldHeight"));
        add(new TextField<TextSubTaskDef>("textFieldWidth"));
    }
}
