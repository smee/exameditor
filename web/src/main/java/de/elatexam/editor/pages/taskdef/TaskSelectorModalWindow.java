package de.elatexam.editor.pages.taskdef;

import net.databinder.components.NullPlug;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

import de.elatexam.model.SubTaskDef;

/**
 * @author Steffen Dienst
 *
 */
public abstract class TaskSelectorModalWindow<T extends SubTaskDef> extends ModalWindow {

  public TaskSelectorModalWindow(String id) {
    super(id);
    setTitle("Bitte wählen Sie den Aufgabentyp");
    setResizable(true);
    setInitialWidth(650);
    setInitialHeight(450);
    setContent(new NullPlug(getContentId()));
  }

	abstract void onSelect(AjaxRequestTarget target, SubTaskDef... subtaskdefs);

  /**
   * @param clazz
   */
  public void showFor(Class<T> clazz, final AjaxRequestTarget target) {
    setContent(new TaskSelectionPanel<T>(getContentId(), clazz) {
      @Override
      public void onSelect(AjaxRequestTarget target,T... subtaskdefs) {
				TaskSelectorModalWindow.this.onSelect(target, subtaskdefs);
        TaskSelectorModalWindow.this.close(target);

      }
    });
    super.show(target);
  }

  /*
   * (non-Javadoc)
   *
   * @see org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow#show(org.apache.wicket.ajax.AjaxRequestTarget)
   */
  @Override
  public void show(AjaxRequestTarget target) {
    showFor((Class<T>) SubTaskDef.class, target);
  }
}