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
package net.kornr.swit.wicket.layout;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A panel that where arbitrary Component objects can be added. Components must be created with the id returned by getContentId(), and must be added
 * with the addContent() method. 
 * @author Rodrigo Reyes
 *
 */
public class ColumnPanel extends Panel 
{
	static public final String CONTENT_ID = "element";
	
	private List<Component> m_elements = new LinkedList<Component>();
	private boolean m_columnsRendered = false;
	private IModel<String> m_defaultInlineCss = null;
	
	public ColumnPanel(String id)
	{
		super(id);
	}

	@Override
	protected void onBeforeRender() 
	{
		if (m_columnsRendered == false)
		{
			ListView<Component> col = new ListView<Component>("column", m_elements) {
				@Override
				protected void populateItem(ListItem<Component> item) 
				{
					Component c = item.getModelObject();
					if (item.contains(c, false) == false)
					{
						item.add(c);
						String css = null;
						if (m_defaultInlineCss!=null)
							css = m_defaultInlineCss.getObject();
						if (css != null)
							item.add(new SimpleAttributeModifier("style", css));
					}
				}
			};
			this.add(col);
			m_columnsRendered = true;
		}

		super.onBeforeRender();
	}
	
	public void addContent(Component comp)
	{
		m_elements.add(comp);
	}
	
	static public String getContentId()
	{		
		return CONTENT_ID;
	}
	
	public int getElementCount()
	{
		return m_elements.size();
	}

	public IModel<String> getDefaultInlineCss() {
		return m_defaultInlineCss;
	}

	public void setDefaultInlineCss(IModel<String> defaultInlineCss) {
		m_defaultInlineCss = defaultInlineCss;
	}

}
