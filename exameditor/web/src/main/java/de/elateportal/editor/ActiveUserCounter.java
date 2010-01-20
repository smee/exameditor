package de.elateportal.editor;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Decrement active user counter when a session expires.
 * 
 * @author Steffen Dienst
 * 
 */
public class ActiveUserCounter implements HttpSessionListener {

  @Override
  public void sessionCreated(final HttpSessionEvent se) {
    TaskEditorApplication.getInstance().incrementUsers();
  }

  @Override
  public void sessionDestroyed(final HttpSessionEvent se) {
    TaskEditorApplication.getInstance().decrementUsers();
  }

}
