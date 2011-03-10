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
package de.elatexam.editor.pages;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import de.elatexam.model.SubTaskDef;

/**
 * @author Steffen Dienst
 * 
 */
public class AddSubtaskDefPanel<T extends SubTaskDef> extends Panel {

	/**
	 * @param id
	 */
	public AddSubtaskDefPanel(String id, final ShowSubtaskDefsPage<T> showSubtaskDefsPage) {
		super(id);
		Link addLink = new Link("add") {
			@Override
			public void onClick() {
				setResponsePage(new EditSubtaskPage<T>(showSubtaskDefsPage.getClazz()));
			}
		};
		add(addLink);
	}

}
