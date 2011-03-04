/*

Copyright (C) 2011 Steffen Dienst

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package de.elatexam.editor.pages.subtaskdefs;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.elatexam.editor.pages.taskdef.TaskSelectionPanel;
import de.elatexam.model.SubTaskDef;

/**
 * @author Steffen Dienst
 *
 */
public class AddSubtaskdefPanel<T extends SubTaskDef> extends Panel {


  private TaskSelectionPanel<T> std;

  /**
   * @param componentId
   * @param rowModel
   * @param taskSelectionPanel
   */
  public AddSubtaskdefPanel(String componentId, IModel<T> rowModel, TaskSelectionPanel<T> taskSelectionPanel) {
    super(componentId, rowModel);
    this.std = taskSelectionPanel;
		add(new AjaxLink<T>("link", rowModel) {
      /*
       * (non-Javadoc)
       *
       * @see org.apache.wicket.ajax.markup.html.AjaxLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
       */
      @Override
      public void onClick(AjaxRequestTarget target) {
				std.onSelect(target, getModelObject());
      }
    });
  }


}
