package de.elateportal.editor.components.panels;

import org.apache.wicket.markup.html.panel.Panel;

import de.elateportal.model.TaskBlockType;

/**
 * @author sdienst
 */
public class TaskBlockPanel extends Panel {

  public TaskBlockPanel(final String id, final Class<? extends TaskBlockType> class1, final TaskBlockType t) {
    super(id);
  }

}

