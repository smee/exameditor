package de.elatexam.editor.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import de.elatexam.editor.pages.subtaskdefs.SubtaskdefTable;
import de.elatexam.model.SubTaskDef;

/**
 * @author sdienst
 */
public class ShowSubtaskDefsPage<T extends SubTaskDef> extends SecurePage {

  public ShowSubtaskDefsPage() {
    this((Class<T>) SubTaskDef.class);
  }

  // TODO use subtaskdefs from current BasicUser
  @SuppressWarnings("unchecked")
  public ShowSubtaskDefsPage(final Class<T> clazz) {
    add(new Label("heading", "Alle Aufgaben"));
    final Link<Void> newTaskLink = new Link<Void>("newTaskdefLink") {
      @Override
      public void onClick() {
        setResponsePage(new EditSubtaskPage<T>(clazz));
      }
    };
    // hide link if this is no specific subtask type
    if (clazz.equals(SubTaskDef.class)) {
      newTaskLink.setVisible(false);
    }

    add(newTaskLink);

    add(new SubtaskdefTable<T>("table", clazz));
  }
}
