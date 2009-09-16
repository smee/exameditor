/*
 * Copyright 2009 Rodrigo Reyes reyes.rr at gmail dot com
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package net.kornr.swit.site.buttoneditor;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class PropertyListEditor extends Panel 
{
	public PropertyListEditor(String id, List<ButtonProperty> model) 
	{
		super(id);
		
		this.add(new ListView<ButtonProperty>("items", model) {
			@Override
			protected void populateItem(ListItem<ButtonProperty> item) 
			{
				ButtonProperty prop = item.getModelObject();
				ButtonPropertyEditorPanel el = new ButtonPropertyEditorPanel("editor", prop, true);
				item.add(el);
			}
		});
	}

}
