package de.elatexam.editor.pages.taskdef;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import de.elatexam.editor.pages.subtaskdefs.SubtaskdefTable;
import de.elatexam.model.SubTaskDef;

/**
 * @author Steffen Dienst
 *
 */
public abstract class TaskSelectionPanel extends Panel {

  public TaskSelectionPanel(String id, Class<? extends SubTaskDef> clazz) {
    super(id);

    add(new SubtaskdefTable<SubTaskDef>("table", (Class<SubTaskDef>) clazz, this));
  }

  abstract public void onSelect(AjaxRequestTarget target, SubTaskDef... subtaskdefs);

}