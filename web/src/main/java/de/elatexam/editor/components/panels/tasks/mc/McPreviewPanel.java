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
package de.elatexam.editor.components.panels.tasks.mc;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import de.elatexam.model.McSubTaskDef.McSubTaskDefAnswerDefinitionsItem;

/**
 * @author Steffen Dienst
 *
 */
public class McPreviewPanel extends Panel {

	/**
	 * @param id
	 * @param model
	 */
	public McPreviewPanel(String id, IModel<?> model) {
		super(id, model);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new ListView<McSubTaskDefAnswerDefinitionsItem>("answerDefinitionsItems") {

			@Override
			protected void populateItem(ListItem<McSubTaskDefAnswerDefinitionsItem> item) {
                McAnswersCorrectnessModel answerCorrectnessModel = new McAnswersCorrectnessModel(item.getModel());
                item.add(new CheckBox("correct", answerCorrectnessModel).setEnabled(false));

                item.add(new Label("value", new McAnswersModel(item.getModel())));
				
			}
		});
		
	}
}
