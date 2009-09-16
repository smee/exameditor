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

import java.io.Serializable;

import net.databinder.components.hib.DataForm;
import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefType;

/**
 * @author Steffen Dienst
 * 
 */
public class SubtaskDefInputPanel extends Panel {
    public class SubtaskDefForm<T extends SubTaskDefType> extends DataForm<T> {

        public SubtaskDefForm(final String id, final Class<T> modelClass) {
            super(id, modelClass);
            init();
        }

        public SubtaskDefForm(final String id, final Class<T> modelClass, final Serializable persistentObjectId) {
            super(id, modelClass, persistentObjectId);
            init();
        }

        public SubtaskDefForm(final String id, final HibernateObjectModel<T> model) {
            super(id, model);
            init();
        }

        protected void delegateSubmit(final Button submittingButton) {
        }

        /**
         * @param string
         * @return
         */
        protected Form<T> getNestedForm(final String string) {
            return null;
        }

        private void init() {
            add(new FeedbackPanel("feedback"));
            add(new TextField<T>("id"));
            add(new TextArea<T>("problem").setRequired(true));
            add(new TextField<T>("hint"));
            add(new TextArea<T>("correctionHint"));
            final Form<T> nestedForm = getNestedForm("nestedForm");
            if (nestedForm != null) {
                add(nestedForm);
            } else {
                add(new EmptyPanel("nestedForm"));
            }
            add(new Button("saveButton"));
            add(new Button("cancelButton") {
                @Override
                public void onSubmit() {
                    clearPersistentObject();
                }
            }.setDefaultFormProcessing(false));
        }

        @Override
        public boolean isTransparentResolver() {
            return true;
        }

        @Override
        protected void onSubmit() {
            super.onSubmit();
            clearPersistentObject();
        }
    }

    public SubtaskDefInputPanel(final String id) {
        super(id);
    }

    public SubtaskDefInputPanel(final String id, final Class<? extends SubTaskDefType> clazz) {
        super(id);
        add(new SubtaskDefForm("taskform", clazz));
    }
}