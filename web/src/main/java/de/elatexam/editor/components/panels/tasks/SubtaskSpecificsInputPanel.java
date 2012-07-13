/*

Copyright (C) 2009 Steffen Dienst

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
package de.elatexam.editor.components.panels.tasks;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import de.elatexam.model.SubTaskDef;

/**
 * Panel that delegates {@link org.apache.wicket.markup.html.form.FormComponent.IVisitor} instances to
 * {@link FormComponent}s within this panel. We need this to allow adding behaviours to formemelements,
 * because the subtask specific {@link FormComponent}s get added in a seperate panel.
 * @author Steffen Dienst
 *
 */
public abstract class SubtaskSpecificsInputPanel<T extends SubTaskDef> extends Panel implements IFormVisitorParticipant {

	/**
	 * @param id
	 */
	public SubtaskSpecificsInputPanel(final String id) {
		super(id);
	}

	/**
	 * @param id
	 * @param model
	 */
    public SubtaskSpecificsInputPanel(final String id, final IModel<T> model) {
		super(id, model);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IFormVisitorParticipant#processChildren()
	 */
	public boolean processChildren() {
	  return true;
  }

	/**
	 * @return
	 */
	public IFormValidator getFormValidator() {
		return null;
	}

}
