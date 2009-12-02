package de.elateportal.editor.components.panels.tasks.cloze;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;

import de.elateportal.editor.components.panels.tasks.SubtaskSpecificsInputPanel;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.TextSubTaskDef;

public class ClozeSubtaskDefInputPanel extends SubtaskSpecificsInputPanel<ClozeSubTaskDef> {

    public ClozeSubtaskDefInputPanel(final String id) {
        super(id);
        add(new TextArea<TextSubTaskDef>("initialTextFieldValue"));
        add(new TextField<TextSubTaskDef>("textFieldHeight"));
        add(new TextField<TextSubTaskDef>("textFieldWidth"));
    }
}
