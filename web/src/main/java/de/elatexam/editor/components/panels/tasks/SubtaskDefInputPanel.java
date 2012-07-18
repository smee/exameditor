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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.databinder.components.hib.DataForm;
import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.wicketstuff.tagit.TagItTextField;

import wicket.contrib.tinymce.TinyMceBehavior;
import wicket.contrib.tinymce.settings.Button;
import wicket.contrib.tinymce.settings.FullScreenPlugin;
import wicket.contrib.tinymce.settings.PastePlugin;
import wicket.contrib.tinymce.settings.SearchReplacePlugin;
import wicket.contrib.tinymce.settings.TablePlugin;
import wicket.contrib.tinymce.settings.TinyMCESettings;
import de.elatexam.editor.components.panels.tasks.cloze.ClozeSubtaskDefInputPanel;
import de.elatexam.editor.components.panels.tasks.mapping.MappingSubtaskDefInputPanel;
import de.elatexam.editor.components.panels.tasks.mc.McSubtaskDefInputPanel;
import de.elatexam.editor.components.panels.tasks.paint.PaintSubtaskDefInputPanel;
import de.elatexam.editor.components.panels.tasks.text.TextSubtaskDefInputPanel;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TextSubTaskDef;
import de.elatexam.model.manual.TaggedSubtaskdef;

/**
 * @author Steffen Dienst
 * 
 */
public class SubtaskDefInputPanel<T extends SubTaskDef> extends Panel {
  /**
   * @author Steffen Dienst
   * 
   * @param <T>
   */
  public class SubtaskDefForm extends DataForm<T> {

    private final Class<T> modelClass;

    public SubtaskDefForm(final String id, final HibernateObjectModel<T> model) {
      super(id, model);
      this.modelClass = (Class<T>) model.getObject().getClass();
      init();
    }

    /**
     * @param id
     * @param modelClass2
     * @return
     */
    private SubtaskSpecificsInputPanel getTaskSpecificFormPanel(final String id) {
      if (modelClass.equals(McSubTaskDef.class))
        return new McSubtaskDefInputPanel(id, (IModel<McSubTaskDef>) getModel());
      else if (modelClass.equals(TextSubTaskDef.class))
        return new TextSubtaskDefInputPanel(id);
      else if (modelClass.equals(MappingSubTaskDef.class))
        return new MappingSubtaskDefInputPanel(id, (IModel<MappingSubTaskDef>) getModel());
      else if (modelClass.equals(ClozeSubTaskDef.class))
        return new ClozeSubtaskDefInputPanel(id, (IModel<ClozeSubTaskDef>) getModel());
      else if (modelClass.equals(PaintSubTaskDef.class))
        return new PaintSubtaskDefInputPanel(id, (IModel<PaintSubTaskDef>) getModel());
      else
        return new SubtaskSpecificsInputPanel<T>(id) {
        };
    }

    /**
     * @param returnPage
     * 
     */
    private void init() {
      add(new FeedbackPanel("feedback"));
      // add common SubTaskDef input fields
      TextField<String> idTf = new TextField<String>("xmlid");
      idTf.add(new AttributeAppender("placeholder", "eindeutiger Bezeichner"));
      add(idTf.setRequired(true));
      final TextArea<String> problemText = new TextArea<String>("problem") {
        @Override
        public IConverter<String> getConverter(Class type) {
          return new IConverter<String>() {

            public String convertToObject(final String text, final Locale locale) {
              return text.replaceAll("<p>", "").replaceAll("</p>", "<br/>");
            }

            public String convertToString(final String value, final Locale locale) {
              return value == null ? "" : value.toString();
            }
          };
        }
      };
      // set the type, else the converter won't get called
      problemText.setType(String.class);
      add(problemText.setRequired(true).add(new TinyMceBehavior(createFullFeatureset())));

      // add subtask input elements
      SubtaskSpecificsInputPanel<T> specificPanel = getTaskSpecificFormPanel("specificelements");
      add(specificPanel);
      IFormValidator fv = specificPanel.getFormValidator();
      if (fv != null)
        add(fv);

      // add correction and hints
      add(new TextField<String>("hint").add(new AttributeAppender("placeholder", "Hinweis für Studenten")));
      add(new TextArea<String>("correctionHint"));
      add(new TagItTextField<String>("tags") {
          private List<String> allTags = de.elatexam.editor.util.Stuff.getAllUniqueTags();
            @Override
            protected Iterable<String> getChoices(final String prefix) {
                List<String> result = new ArrayList<String>();
                for (String tag : allTags) {
                  if(tag.startsWith(prefix))
                    result.add(tag);
                }
                return result; 
            }
            @Override
            public IConverter<Set<String>> getConverter(Class type) {
              return new TagSetConverter();
            }
          });
      add(new org.apache.wicket.markup.html.form.Button("saveButton"));
      add(new org.apache.wicket.markup.html.form.Button("cancelButton") {
        @Override
        public void onSubmit() {
          clearPersistentObject();
          // setResponsePage(returnPage);
        }
      }.setDefaultFormProcessing(false));
    }

//    /* (non-Javadoc)
//     * @see net.databinder.components.hib.DataForm#savePersistentObjectIfNew()
//     */
//    @Override
//    protected boolean savePersistentObjectIfNew() {
//      // TODO Auto-generated method stub
//      return super.savePersistentObjectIfNew();
//    }
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

  public SubtaskDefInputPanel(final String id, final HibernateObjectModel<T> model) {
    super(id);
    add(new SubtaskDefForm("taskform", model));
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
