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
package de.elatexam.editor.components.panels.tasks.mapping;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.MappingSubTaskDef.Assignment;
import de.elatexam.model.MappingSubTaskDef.Concept;

/**
 * @author Steffen Dienst
 *
 */
public class MappingPreviewPanel extends Panel {

	/**
	 * @param id
	 * @param model
	 */
	public MappingPreviewPanel(String id, IModel<?> model) {
		super(id, model);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		MappingSubTaskDef mstd = (MappingSubTaskDef) getDefaultModelObject();
		final List<Assignment> ass = mstd.getAssignment();
		
		add(new ListView<Concept>("concept") {

			@Override
			protected void populateItem(ListItem<Concept> item) {
				item.add(new Label("name",item.getModelObject().getName()).setEscapeModelStrings(false));	
				item.add(new DropDownChoice<Assignment>("assignment", ass){
					@Override
					public IConverter getConverter(Class<?> type) {
						return new IConverter() {
							
							@Override
							public String convertToString(Object value, Locale locale) {
								return ((Assignment)value).getName();
							}
							
							@Override
							public Object convertToObject(String value, Locale locale) {
								return null;
							}
						};
					}
				});
			}
			
		});
	}

}
