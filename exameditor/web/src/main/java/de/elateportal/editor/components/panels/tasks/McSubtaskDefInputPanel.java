package de.elateportal.editor.components.panels.tasks;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.validator.MinimumValidator;

import de.thorstenberger.taskmodel.complex.complextaskdef.TextSubTaskDef;

public class McSubtaskDefInputPanel extends Panel {

    public McSubtaskDefInputPanel(final String id) {
        super(id);
        add(new TextField<TextSubTaskDef>("displayedAnswers").add(new MinimumValidator(1)));
        add(new TextField<TextSubTaskDef>("maxCorrectAnswers"));
        add(new TextField<TextSubTaskDef>("minCorrectAnswers"));
        add(new CheckBox("preserveOrderOfAnswers"));
        // TODO answers!
    }
}
