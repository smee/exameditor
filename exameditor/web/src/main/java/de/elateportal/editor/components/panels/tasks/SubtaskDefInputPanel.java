/*

Copyright (C) 2009 Steffen Dienst

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package de.elateportal.editor.components.panels.tasks;

import net.databinder.components.hib.DataForm;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.MappingSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.McSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.PaintSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.complextaskdef.TextSubTaskDef;

/**
 * @author Steffen Dienst
 * 
 */
public class SubtaskDefInputPanel extends Panel {
    /**
     * @author Steffen Dienst
     * 
     * @param <T>
     */
    public class SubtaskDefForm<T extends SubTaskDefType> extends DataForm<T> {

        public SubtaskDefForm(final String id, final Class<T> modelClass) {
            super(id, modelClass);
            init(modelClass);
        }

        /**
         * @param submittingButton
         */
        protected void delegateSubmit(final Button submittingButton) {
        }

        /**
         * @param id
         * @param modelClass
         * @return
         */
        private Component getTaskSpecificFormPanel(final String id, final Class<T> modelClass) {
            if (modelClass.equals(McSubTaskDef.class)) {
                return new McSubtaskDefInputPanel(id);
            } else if (modelClass.equals(TextSubTaskDef.class)) {
                return new TextSubtaskDefInputPanel(id);
            } else if (modelClass.equals(MappingSubTaskDef.class)) {
                return new MappingSubtaskDefInputPanel(id);
            } else if (modelClass.equals(ClozeSubTaskDef.class)) {
                return new ClozeSubtaskDefInputPanel(id);
            } else if (modelClass.equals(PaintSubTaskDef.class)) {
                return new PaintSubtaskDefInputPanel(id);
            } else {
                return new EmptyPanel(id);
            }
        }

        /**
         * @param modelClass
         * 
         */
        private void init(final Class<T> modelClass) {
            add(new FeedbackPanel("feedback"));
            add(new TextField<T>("id").setRequired(true));
            add(new TextArea<T>("problem").setRequired(true));
            add(new TextField<T>("hint"));
            add(new TextArea<T>("correctionHint"));
            add(new Button("saveButton"));
            add(new Button("cancelButton") {
                @Override
                public void onSubmit() {
                    clearPersistentObject();
                }
            }.setDefaultFormProcessing(false));
            add(getTaskSpecificFormPanel("specificelements", modelClass));
        }

        /*
         * (non-Javadoc)
         * 
         * @see net.databinder.components.hib.DataForm#onSubmit()
         */
        @Override
        protected void onSubmit() {
            super.onSubmit();
            clearPersistentObject();
        }
    }

    /**
     * @param id
     */
    public SubtaskDefInputPanel(final String id) {
        super(id);
    }

    /**
     * @param id
     * @param clazz
     */
    public SubtaskDefInputPanel(final String id, final Class<? extends SubTaskDefType> clazz) {
        super(id);
        add(new SubtaskDefForm("taskform", clazz));
    }
}
