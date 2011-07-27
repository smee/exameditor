package de.elatexam.editor.components.panels;

import java.util.Calendar;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.RequestLogger.SessionData;

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
        int c = 0;
        long thirtyMinsAgo = System.currentTimeMillis() - 15*60*1000;
        for(SessionData session: TaskEditorApplication.getInstance().getRequestLogger().getLiveSessions())
          if(session.getLastActive().getTime() > thirtyMinsAgo)
            c++;
        return c;
      }
    }).setRenderBodyOnly(true));
  }

}

