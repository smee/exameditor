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
import net.kornr.swit.wicket.layout.LayoutInfo;
import net.kornr.swit.wicket.layout.ThreeColumnsLayoutManager;
import net.kornr.swit.wicket.layout.ThreeColumnsLayoutManager.ColumnLayout;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.value.ValueMap;


public class ThreeColumnsLayoutBorderFixed extends Border implements IHeaderContributor, ThreeColumnsLayoutManager.ColumnLayout
{
	static Integer s_zero = new Integer(0);
	
	private LayoutInfo m_layout;
	private ColumnPanel m_left, m_right;

	private WebMarkupContainer m_colmaskholygrail;
	private WebMarkupContainer m_colmid;
	private WebMarkupContainer m_colleft;
	private WebMarkupContainer m_col1wrap;
	private WebMarkupContainer m_col1;
	private WebMarkupContainer m_col2;
	private WebMarkupContainer m_col3;

	public ThreeColumnsLayoutBorderFixed(String id, LayoutInfo layout) 
	{
		super(id);
		m_layout = layout;
		
		m_colmaskholygrail = new WebMarkupContainer("colmask_holygrail");
		m_colmid = new WebMarkupContainer("colmid");
		m_colleft = new WebMarkupContainer("colleft");
		m_col1wrap = new WebMarkupContainer("col1wrap");
		m_col1 = new WebMarkupContainer("col1");
		m_col2 = new WebMarkupContainer("col2");
		m_col3 = new WebMarkupContainer("col3");

		m_colmaskholygrail.add(m_colmid);
		m_colmid.add(m_colleft);
		m_colleft.add(m_col1wrap);
		m_col1wrap.add(m_col1);
		m_colleft.add(m_col2);
		m_colleft.add(m_col3);
		
		m_left = new ColumnPanel("leftcolumn");
		m_right = new ColumnPanel("rightcolumn");
		
		m_col2.add(m_left);
		m_col3.add(m_right);
		
		this.add(m_colmaskholygrail);
		m_col1.add(getBodyContainer());

		///
		/// Now, sets the classes
		
		ValueMap m = layout.getClassId();
		m_colmaskholygrail.add(new AttributeAppender("class", new Model<String>(m.getString("colmask")+" "+m.getString("holygrail")), " "));
		m_colmid.add(new AttributeAppender("class", new Model<String>(m.getString("colmid")), " "));
		m_colleft.add(new AttributeAppender("class", new Model<String>(m.getString("colleft")), " "));
		m_col1wrap.add(new AttributeAppender("class", new Model<String>(m.getString("col1wrap")), " "));
		m_col1.add(new AttributeAppender("class", new Model<String>(m.getString("col1")), " "));
		m_col2.add(new AttributeAppender("class", new Model<String>(m.getString("col2")), " "));
		m_col3.add(new AttributeAppender("class", new Model<String>(m.getString("col3")), " "));
	}

	public void renderHead(IHeaderResponse response) 
	{
		boolean zeroLeft = m_left.getElementCount()==0;
		boolean zeroRight = m_right.getElementCount()==0;
		
		response.renderCSSReference(ThreeColumnsLayoutResource.urlFor(m_layout, zeroLeft?s_zero:null, zeroRight?s_zero:null, null));
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
