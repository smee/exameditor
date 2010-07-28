package de.elatexam.editor;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import de.thorstenberger.taskmodel.TaskModelViewDelegate;

/**
 * Remove all task previews from memory.
 *
 * @author Steffen Dienst
 *
 */
public class RemovePreviews implements HttpSessionListener {

  @Override
  public void sessionCreated(final HttpSessionEvent se) {
  }

  @Override
  public void sessionDestroyed(final HttpSessionEvent se) {
    TaskModelViewDelegate.removeSession(se.getSession().getId());
  }

}
