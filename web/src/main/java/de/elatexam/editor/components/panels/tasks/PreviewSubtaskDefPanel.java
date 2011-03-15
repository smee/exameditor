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
package de.elatexam.editor.components.panels.tasks;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import de.elatexam.editor.components.panels.tasks.cloze.ClozePreviewPanel;
import de.elatexam.editor.components.panels.tasks.mapping.MappingPreviewPanel;
import de.elatexam.editor.components.panels.tasks.mc.McPreviewPanel;
import de.elatexam.editor.components.panels.tasks.text.TextPreviewPanel;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TextSubTaskDef;

/**
 * @author Steffen Dienst
 *
 */
public class PreviewSubtaskDefPanel<T extends SubTaskDef> extends Panel {

	/**
	 * @param id
	 */
	public PreviewSubtaskDefPanel(String id) {
		super(id);
	}

	/**
	 * @param id
	 * @param model
	 */
	public PreviewSubtaskDefPanel(String id, IModel<T> model) {
		super(id, new CompoundPropertyModel<T>(model));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		Label question = new Label("problem");
		question.setEscapeModelStrings(false);
		add(question);
		add(getTypeSpecificPanel("typespecific",(IModel<? extends SubTaskDef>) getDefaultModel()));
	}

	/**
	 * @return
	 */
	public static Component getTypeSpecificPanel(String id,IModel<? extends SubTaskDef> m) {
		IModel<?> model = new CompoundPropertyModel(m);
		Object obj = model.getObject();
		
		if(McSubTaskDef.class.isInstance(obj)){
			return new McPreviewPanel(id, model);
		}else if(ClozeSubTaskDef.class.isInstance(obj)){
			return new ClozePreviewPanel(id, model);
		}else if(TextSubTaskDef.class.isInstance(obj)){
			return new TextPreviewPanel(id, model);
		}else if(MappingSubTaskDef.class.isInstance(obj)){
			return new MappingPreviewPanel(id, model);
		}else
			return new EmptyPanel(id);
	}

}
