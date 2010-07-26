package de.elatexam.editor.components.panels.tasks.cloze;

import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.markup.html.form.TextField;

import de.elatexam.editor.components.panels.tasks.TaskBlockConfigPanel;
import de.elatexam.model.ClozeTaskBlock;

/**
 * @author sdienst
 */
public class ClozeTaskBlockConfigPanel extends TaskBlockConfigPanel<ClozeTaskBlock> {

  public ClozeTaskBlockConfigPanel(String id, HibernateObjectModel<ClozeTaskBlock> model) {
    super(id, model);
  }

  @Override
  protected void addSpecifics() {
    add(new TextField("clozeConfig.negativePoints"));
  }

}

