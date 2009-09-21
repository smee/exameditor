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
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.settings.Button;
import wicket.contrib.tinymce.settings.FullScreenPlugin;
import wicket.contrib.tinymce.settings.PastePlugin;
import wicket.contrib.tinymce.settings.SearchReplacePlugin;
import wicket.contrib.tinymce.settings.TablePlugin;
import wicket.contrib.tinymce.settings.TinyMCESettings;
import de.elateportal.editor.behaviours.TextFieldHintBehaviour;
import de.elateportal.editor.pages.ShowSubtaskDefsPage;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.SubTaskDefType;
import de.elateportal.model.TextSubTaskDef;

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

        private final Class<? extends SubTaskDefType> modelClass;

        public SubtaskDefForm(final String id, final Class<T> modelClass) {
            super(id, modelClass);
            this.modelClass = modelClass;
            init();
        }

        /**
         * @param submittingButton
         */
        protected void delegateSubmit(final org.apache.wicket.markup.html.form.Button submittingButton) {
        }

        /**
         * @param id
         * @param modelClass2
         * @return
         */
        private Component getTaskSpecificFormPanel(final String id) {
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
         * 
         */
        private void init() {
            add(new FeedbackPanel("feedback"));
            // add common subtaskdeftype input fields
            add(new TextField<T>("id").setRequired(true).add(new TextFieldHintBehaviour(Model.of("Eindeutiger Bezeichner"))));
            add(new TextArea<T>("problem").setRequired(true).add(new TinyMceBehavior(createFullFeatureset())));
            add(new TextField<T>("hint"));
            add(new TextArea<T>("correctionHint"));
            add(new org.apache.wicket.markup.html.form.Button("saveButton"));
            add(new org.apache.wicket.markup.html.form.Button("cancelButton") {
                @Override
                public void onSubmit() {
                    clearPersistentObject();
                    setResponsePage(new ShowSubtaskDefsPage(modelClass));
                }
            }.setDefaultFormProcessing(false));
            // add subtask input elements
            add(getTaskSpecificFormPanel("specificelements"));
        }

        /*
         * (non-Javadoc)
         * 
         * @see net.databinder.components.hib.DataForm#onSubmit()
         */
        @Override
        protected void onSubmit() {
            // TODO clear input fields that have the hint text as content, set them to empty
            super.onSubmit();
            clearPersistentObject();
            setResponsePage(new ShowSubtaskDefsPage(modelClass));
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

    /**
     * Add all tinymce features to the rich text editor.
     * 
     * @return
     */
    public TinyMCESettings createFullFeatureset() {
        final TinyMCESettings settings = new TinyMCESettings(TinyMCESettings.Theme.advanced);

        // first toolbar
        settings.add(Button.fontselect, TinyMCESettings.Toolbar.first, TinyMCESettings.Position.after);
        settings.add(Button.fontsizeselect, TinyMCESettings.Toolbar.first, TinyMCESettings.Position.after);

        // second toolbar
        final PastePlugin pastePlugin = new PastePlugin();
        final SearchReplacePlugin searchReplacePlugin = new SearchReplacePlugin();
        settings.add(Button.cut, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(Button.copy, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(pastePlugin.getPasteButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(pastePlugin.getPasteTextButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(pastePlugin.getPasteWordButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(Button.separator, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
        settings.add(searchReplacePlugin.getSearchButton(), TinyMCESettings.Toolbar.second,
                TinyMCESettings.Position.before);
        settings.add(searchReplacePlugin.getReplaceButton(), TinyMCESettings.Toolbar.second,
                TinyMCESettings.Position.before);

        settings.add(Button.separator, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(Button.forecolor, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
        settings.add(Button.backcolor, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);

        // third toolbar
        final TablePlugin tablePlugin = new TablePlugin();
        final FullScreenPlugin fullScreenPlugin = new FullScreenPlugin();
        settings.add(tablePlugin.getTableControls(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.before);
        settings.add(Button.separator, TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
        settings.add(Button.separator, TinyMCESettings.Toolbar.third, TinyMCESettings.Position.before);
        settings.add(fullScreenPlugin.getFullscreenButton(), TinyMCESettings.Toolbar.third,
                TinyMCESettings.Position.after);

        // other settings
        settings.setToolbarAlign(TinyMCESettings.Align.left);
        settings.setToolbarLocation(TinyMCESettings.Location.top);
        settings.setStatusbarLocation(TinyMCESettings.Location.bottom);
        settings.setResizing(true);

        // remove superflous buttons
        settings.disableButton(Button.image);
        settings.disableButton(Button.anchor);
        settings.disableButton(Button.link);
        settings.disableButton(Button.unlink);
        settings.disableButton(Button.visualaid);
        settings.disableButton(Button.cleanup);
        settings.disableButton(Button.help);

        return settings;
    }
}
