package de.elateportal.editor.components.panels.tasks;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;

import de.elateportal.editor.components.panels.tasks.SubtaskDefInputPanel.SubtaskDefForm;
import de.thorstenberger.taskmodel.complex.complextaskdef.TextSubTaskDef;

/**
 * @author sdienst
 */
public class TextSubtaskDefInputPanel extends Panel {
    protected class TextSubtaskDefForm extends Form<TextSubTaskDef> {

        public TextSubtaskDefForm(final String id) {
            super(id);

            add(new TextArea<TextSubTaskDef>("initialTextFieldValue"));
            add(new TextField<TextSubTaskDef>("textFieldWidth"));
            add(new TextField<TextSubTaskDef>("textFieldHeight"));
        }

    }

    public TextSubtaskDefInputPanel(final String id) {
        super(id);

        add(new SubtaskDefForm("taskform", TextSubTaskDef.class));
        final TextSubtaskDefForm form = new TextSubtaskDefForm(id);
        add(form);
    }
}
