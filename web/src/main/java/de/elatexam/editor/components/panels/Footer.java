package de.elatexam.editor.components.panels;

import java.util.Calendar;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

import de.elatexam.editor.TaskEditorApplication;

/**
 * @author sdienst
 */
public class Footer extends Panel {

  public Footer(final String id) {
    super(id);

    add(new Label("currentyear", Model.of(Calendar.getInstance().get(Calendar.YEAR))).setRenderBodyOnly(true));
    add(new Label("usercount", new AbstractReadOnlyModel<Integer>() {
      @Override
      public Integer getObject() {
        return TaskEditorApplication.getInstance().getActiveUsersCount();
      }
    }).setRenderBodyOnly(true));
  }

}

