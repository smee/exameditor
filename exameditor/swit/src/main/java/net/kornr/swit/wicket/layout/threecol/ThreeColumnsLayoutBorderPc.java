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


public class ThreeColumnsLayoutBorderPc extends Border implements IHeaderContributor, ThreeColumnsLayoutManager.ColumnLayout
{
	static Integer s_zero = new Integer(0);

	private LayoutInfo m_layout;
	private ColumnPanel m_left, m_right;

	private WebMarkupContainer m_colmaskthreecol;
	private WebMarkupContainer m_colmid;
	private WebMarkupContainer m_colleft;
	private WebMarkupContainer m_col1;
	private WebMarkupContainer m_col2;
	private WebMarkupContainer m_col3;
	
	public ThreeColumnsLayoutBorderPc(String id, LayoutInfo layout) {
		super(id);
		
		m_layout = layout;
		
		m_left = new ColumnPanel("leftcolumn");
		m_right = new ColumnPanel("rightcolumn");
		
		m_colmaskthreecol = new WebMarkupContainer("colmask_threecol");
		m_colmid = new WebMarkupContainer("colmid");
		m_colleft = new WebMarkupContainer("colleft");
		m_col1 = new WebMarkupContainer("col1");
		m_col2 = new WebMarkupContainer("col2");
		m_col3 = new WebMarkupContainer("col3");

		this.add(m_colmaskthreecol);
		m_colmaskthreecol.add(m_colmid);
		m_colmid.add(m_colleft);
		m_colleft.add(m_col1);
		m_colleft.add(m_col2);
		m_colleft.add(m_col3);
		
		m_col1.add(getBodyContainer());

		m_col2.add(m_left);
		m_col3.add(m_right);
		
		ValueMap m = layout.getClassId();
		m_colmaskthreecol.add(new AttributeAppender("class", new Model<String>(m.getString("colmask")+" "+m.getString("threecol")), " "));
		m_colmid.add(new AttributeAppender("class", new Model<String>(m.getString("colmid")), " "));
		m_colleft.add(new AttributeAppender("class", new Model<String>(m.getString("colleft")), " "));
		m_col1.add(new AttributeAppender("class", new Model<String>(m.getString("col1")), " "));
		m_col2.add(new AttributeAppender("class", new Model<String>(m.getString("col2")), " "));
		m_col3.add(new AttributeAppender("class", new Model<String>(m.getString("col3")), " "));
	}

	public void renderHead(IHeaderResponse response) {
		boolean zeroLeft = m_left.getElementCount()==0;
		boolean zeroRight = m_right.getElementCount()==0;
		response.renderCSSReference(ThreeColumnsLayoutResource.urlFor(m_layout, zeroLeft?s_zero:null, zeroRight?s_zero:null, null));

//		if (m_left.getElementCount()==0 || m_right.getElementCount()==0)
//		{
//			ValueMap params = new ValueMap();
//			if (m_left.getElementCount()==0)
//				params.add("left", "0");
//			if (m_right.getElementCount()==0)
//				params.add("right", "0");
//			
//
//			response.renderCSSReference(RequestCycle.get().urlFor(new ResourceReference(m_layout.getName()), params).toString());
//		}
//		else
//		{
//			response.renderCSSReference(new ResourceReference(m_layout.getName()));		
//		}		
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
