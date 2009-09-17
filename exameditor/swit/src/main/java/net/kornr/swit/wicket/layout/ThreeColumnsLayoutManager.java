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

import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutBorderFixed;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutBorderPc;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;

/**
 * A multi-column layout manager, based on http://matthewjamestaylor.com/blog/perfect-multi-column-liquid-layouts
 * 
 * @author Rodrigo Reyes
 *
 */
public class ThreeColumnsLayoutManager extends Border 
{
	public static interface ColumnLayout
	{
		public ColumnPanel getLeftColumn();
		public ColumnPanel getRightColumn();
	}
	
	private ColumnLayout m_border;
	
	public ThreeColumnsLayoutManager(String id, LayoutInfo layout) 
	{
		super(id);

		if (layout.getUnit() == LayoutInfo.UNIT_PERCENTAGE)
			m_border = new ThreeColumnsLayoutBorderPc("border", layout);
		else
			m_border = new ThreeColumnsLayoutBorderFixed("border", layout);
		
		this.add((Border)m_border);
	}

	public ColumnPanel getLeftColumn()
	{
		return m_border.getLeftColumn();
	}

	public ColumnPanel getRightColumn()
	{
		return m_border.getRightColumn();
	}

}
