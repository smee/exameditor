package de.elatexam.editor.pages.taskdef;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;

import de.elatexam.editor.pages.subtaskdefs.SubtaskdefTable;
import de.elatexam.model.SubTaskDef;

/**
 * @author Steffen Dienst
 *
 */
public abstract class TaskSelectionPanel<T extends SubTaskDef> extends Panel {

  public TaskSelectionPanel(String id, Class<T> clazz) {
    super(id);

    add(new SubtaskdefTable<T>("table", clazz, this));
  }

  abstract public void onSelect(AjaxRequestTarget target, T... subtaskdefs);

}