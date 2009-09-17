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
package net.kornr.swit.wicket.layout.threecol;

import net.kornr.swit.wicket.layout.ColumnPanel;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.value.ValueMap;


public class ThreeColumnsLayoutPanel extends Panel implements IHeaderContributor
{
	private String m_layoutName;
	private ColumnPanel m_left, m_center, m_right;
	
	public ThreeColumnsLayoutPanel(String id, String layoutName) 
	{
		super(id);
		m_layoutName = layoutName;

		m_left = new ColumnPanel("leftcolumn");
		m_center = new ColumnPanel("centercolumn");
		m_right = new ColumnPanel("rightcolumn");
		
		this.add(m_left);
		this.add(m_center);
		this.add(m_right);
	}

	public void renderHead(IHeaderResponse response) 
	{
		if (m_left.getElementCount()==0 || m_right.getElementCount()==0)
		{
			ValueMap params = new ValueMap();
			if (m_left.getElementCount()==0)
				params.add("left", "0");
			if (m_right.getElementCount()==0)
				params.add("right", "0");
			
			response.renderCSSReference(RequestCycle.get().urlFor(new ResourceReference(m_layoutName), params).toString());
		}
		else
		{
			response.renderCSSReference(new ResourceReference(m_layoutName));		
		}
	}

	public ColumnPanel getMiddleColumn()
	{
		return m_center;
	}

	public ColumnPanel getLeftColumn()
	{
		return m_left;
	}

	public ColumnPanel getRightColumn()
	{
		return m_right;
	}

}
