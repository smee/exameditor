package de.elatexam.editor.components.panels.tasks.paint;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;

import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.TextSubTaskDef;
import de.elatexam.editor.components.panels.tasks.SubtaskSpecificsInputPanel;

public class PaintSubtaskDefInputPanel extends SubtaskSpecificsInputPanel<PaintSubTaskDef> {

    public PaintSubtaskDefInputPanel(final String id) {
        super(id);
        // http://mrhaki.blogspot.com/2009/05/wicket-component-for-java-deployment.html
        add(new TextArea<TextSubTaskDef>("initialTextFieldValue"));
        add(new TextField<TextSubTaskDef>("textFieldHeight"));
        add(new TextField<TextSubTaskDef>("textFieldWidth"));
    }
}
