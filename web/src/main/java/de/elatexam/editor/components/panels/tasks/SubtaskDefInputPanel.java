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
package de.elatexam.editor.components.panels.tasks;

import java.util.Locale;

import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.settings.Button;
import wicket.contrib.tinymce.settings.FullScreenPlugin;
import wicket.contrib.tinymce.settings.PastePlugin;
import wicket.contrib.tinymce.settings.SearchReplacePlugin;
import wicket.contrib.tinymce.settings.TablePlugin;
import wicket.contrib.tinymce.settings.TinyMCESettings;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.SubTaskDefType;
import de.elatexam.model.TextSubTaskDef;
import de.elatexam.editor.components.form.ShinyForm;
import de.elatexam.editor.components.panels.tasks.cloze.ClozeSubtaskDefInputPanel;
import de.elatexam.editor.components.panels.tasks.mapping.MappingSubtaskDefInputPanel;
import de.elatexam.editor.components.panels.tasks.mc.McSubtaskDefInputPanel;
import de.elatexam.editor.components.panels.tasks.paint.PaintSubtaskDefInputPanel;
import de.elatexam.editor.components.panels.tasks.text.TextSubtaskDefInputPanel;

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
  public class SubtaskDefForm<T extends SubTaskDefType> extends ShinyForm<T> {

    private final Class<? extends SubTaskDefType> modelClass;

    public SubtaskDefForm(final String id, final Class<T> modelClass) {
      super(id, modelClass);
      this.modelClass = modelClass;
      init();
    }

    public SubtaskDefForm(final String id, final HibernateObjectModel<T> model) {
      super(id, model);
      this.modelClass = model.getObject().getClass();
      init();
    }

    public SubtaskDefForm(final String id, final T object) {
      super(id, new HibernateObjectModel<T>(object));
      this.modelClass = object.getClass();
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
        return new McSubtaskDefInputPanel(id, (IModel<McSubTaskDef>) getModel());
      } else if (modelClass.equals(TextSubTaskDef.class)) {
        return new TextSubtaskDefInputPanel(id);
      } else if (modelClass.equals(MappingSubTaskDef.class)) {
        return new MappingSubtaskDefInputPanel(id, (IModel<MappingSubTaskDef>) getModel());
      } else if (modelClass.equals(ClozeSubTaskDef.class)) {
        return new ClozeSubtaskDefInputPanel(id, (IModel<ClozeSubTaskDef>) getModel());
      } else if (modelClass.equals(PaintSubTaskDef.class)) {
        return new PaintSubtaskDefInputPanel(id);
      } else {
        return new EmptyPanel(id);
      }
    }

    /**
     * @param returnPage
     * 
     */
    private void init() {
      add(new FeedbackPanel("feedback"));
      // add common subtaskdeftype input fields
      add(new TextField<String>("xmlid").setRequired(true));// .add(new
      // TextFieldHintBehaviour(Model.of("Eindeutiger Bezeichner"))));
      final TextArea<String> problemText = new TextArea<String>("problem") {
        @Override
        public IConverter getConverter(final Class<?> type) {
          return new IConverter() {

            public Object convertToObject(final String text, final Locale locale) {
              return text.replaceAll("<p>", "").replaceAll("</p>", "<br/>");
            }

            public String convertToString(final Object value, final Locale locale) {
              return value == null ? "" : value.toString();
            }
          };
        }
      };
      // set the type, else the converter won't get called
      problemText.setType(String.class);
      add(problemText.setRequired(true).add(new TinyMceBehavior(createFullFeatureset())));

      // add subtask input elements
      add(getTaskSpecificFormPanel("specificelements"));

      // add correction and hints
      add(new TextField<String>("hint"));
      add(new TextArea<String>("correctionHint"));
      add(new org.apache.wicket.markup.html.form.Button("saveButton"));
      add(new org.apache.wicket.markup.html.form.Button("cancelButton") {
        @Override
        public void onSubmit() {
          clearPersistentObject();
          // setResponsePage(returnPage);
        }
      }.setDefaultFormProcessing(false));
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.databinder.components.hib.DataForm#onSubmit()
     */
    @Override
    protected void onSubmit() {
      super.onSubmit();
      info("Gespeichert!");
      // clearPersistentObject();
      // setResponsePage(new ShowSubtaskDefsPage(modelClass));
    }

  }

  /**
   * @param id
   * @param returnPage
   * @param clazz
   */
  public SubtaskDefInputPanel(final String id, final Class<? extends SubTaskDefType> clazz,
      final SubTaskDefType object) {
    super(id);
    if (object != null) {
      add(new SubtaskDefForm("taskform", object));
    } else {
      add(new SubtaskDefForm("taskform", clazz));
    }
  }

  public SubtaskDefInputPanel(final String id, final HibernateObjectModel<SubTaskDefType> model) {
    super(id);
    add(new SubtaskDefForm<SubTaskDefType>("taskform", model));
  }

  /**
   * Add all tinymce features to the rich text editor.
   * 
   * @return
   */
  public static TinyMCESettings createFullFeatureset() {
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
    settings.add(searchReplacePlugin.getSearchButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);
    settings.add(searchReplacePlugin.getReplaceButton(), TinyMCESettings.Toolbar.second, TinyMCESettings.Position.before);

    settings.add(Button.separator, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
    settings.add(Button.forecolor, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);
    settings.add(Button.backcolor, TinyMCESettings.Toolbar.second, TinyMCESettings.Position.after);

    // third toolbar
    final TablePlugin tablePlugin = new TablePlugin();
    final FullScreenPlugin fullScreenPlugin = new FullScreenPlugin();
    settings.add(tablePlugin.getTableControls(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.before);
    settings.add(Button.separator, TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);
    settings.add(Button.separator, TinyMCESettings.Toolbar.third, TinyMCESettings.Position.before);
    settings.add(fullScreenPlugin.getFullscreenButton(), TinyMCESettings.Toolbar.third, TinyMCESettings.Position.after);

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