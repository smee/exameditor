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
package de.elatexam.editor.components.panels.tasks.text;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

import de.elatexam.model.TextSubTaskDef;
import de.elatexam.model.ClozeSubTaskDef.Cloze;

/**
 * @author Steffen Dienst
 *
 */
public class TextPreviewPanel extends Panel {

	/**
	 * @param id
	 * @param model
	 */
	public TextPreviewPanel(String id, IModel<?> model) {
		super(id, model);
	}
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		 add(new TextArea<TextSubTaskDef>("initialTextFieldValue").setEnabled(false));
	}

}
