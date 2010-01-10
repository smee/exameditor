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
package de.elateportal.editor.pages;

import net.databinder.auth.hib.AuthDataSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.util.ListModel;

import de.elateportal.editor.components.panels.PreviewPanel;
import de.elateportal.editor.components.panels.tasks.SubtaskDefInputPanel;
import de.elateportal.editor.components.panels.tree.ComplexTaskDefTree;
import de.elateportal.editor.components.panels.tree.ComplexTaskdefTreeProvider;
import de.elateportal.editor.user.BasicUser;
import de.elateportal.model.ComplexTaskDef;
import de.elateportal.model.SubTaskDefType;

/**
 * @author Steffen Dienst
 * 
 */
public class TaskDefPage extends SecurePage {

	private Panel editPanel;
	private ComplexTaskDefTree tree;
	private final PreviewPanel previewPanel;

	public TaskDefPage() {
		// TODO use detachablemodel that delegates to current user
		add(tree = new ComplexTaskDefTree("tree", this, new ComplexTaskdefTreeProvider(new ListModel<ComplexTaskDef>(
		    ((BasicUser) AuthDataSession.get().getUser()).getTaskdefs()))));
		previewPanel = new PreviewPanel("editpanel", tree);
		editPanel = previewPanel;
		add(editPanel.setOutputMarkupId(true));
	}

	public void renderPanelFor(ComplexTaskDef t, AjaxRequestTarget target) {
		editPanel.replaceWith(previewPanel);
		editPanel = previewPanel;
		target.addComponent(editPanel);
	}

	/**
	 * @param subtask
	 * @param target
	 */
	public void renderPanelFor(SubTaskDefType subtask, AjaxRequestTarget target) {
		SubtaskDefInputPanel edit = new SubtaskDefInputPanel("editpanel", subtask.getClass(), subtask);
		edit.setOutputMarkupId(true);
		editPanel.replaceWith(edit);
		editPanel = edit;
		target.addComponent(editPanel);
	}
}
