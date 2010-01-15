package de.elateportal.editor.components.panels;

import java.util.Calendar;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

import de.elateportal.editor.TaskEditorApplication;

/**
 * @author sdienst
 */
public class Footer extends Panel {

  public Footer(final String id) {
    super(id);

    add(new Label("currentyear", Model.of(Calendar.getInstance().get(Calendar.YEAR))));
    add(new Label("usercount", new AbstractReadOnlyModel<Integer>() {
      @Override
      public Integer getObject() {
        return TaskEditorApplication.getInstance().getActiveUsersCount();
      }
    }));
  }

}

