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
package net.kornr.swit.site;

import java.awt.Color;
import java.awt.Font;

import net.kornr.swit.button.ButtonResource;
import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.button.effect.Rotate;
import net.kornr.swit.button.effect.ShadowBorder;
import net.kornr.swit.wicket.border.TableImageBorder;
import net.kornr.swit.wicket.border.graphics.GlossyRoundedBorderMaker;
import net.kornr.swit.wicket.border.graphics.RoundedBorderMaker;
import net.kornr.swit.wicket.layout.ColumnPanel;
import net.kornr.swit.wicket.layout.LayoutInfo;
import net.kornr.swit.wicket.layout.ThreeColumnsLayoutManager;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutBorderFixed;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutBorderPc;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutResource;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.Model;


public class BasePage extends WebPage implements IHeaderContributor
{
	static private Color s_innerColor = new Color(0xF8,0xf8,0xf7);
	static private Long s_border = RoundedBorderMaker.register(20, 3.5f, new Color(0xC5,0xC5,0xC5), s_innerColor);

	static private ButtonTemplate s_logoTemplate = new net.kornr.swit.button.VistafarianButton(); 
	static {
		s_logoTemplate.setFont(new Font("Verdana", Font.BOLD|Font.ITALIC, 48));
		s_logoTemplate.setWidth(200);
		s_logoTemplate.setHeight(60);
		s_logoTemplate.setFontColor(new Color(0xFFFFFF));
		s_logoTemplate.setAutoExtend(Boolean.TRUE);
		s_logoTemplate.setShadowDisplayed(Boolean.TRUE);
		s_logoTemplate.addEffect(new ShadowBorder(8,0,0,Color.black));
		s_logoTemplate.addEffect(new Rotate(-0.1d));
	}
	
	static private LayoutInfo s_layout = new LayoutInfo(LayoutInfo.UNIT_PIXEL, 250, 0);
	static {
		ThreeColumnsLayoutResource.register(s_layout);
		s_layout.setLeftColor(new Color(0xCCCCFF));
		s_layout.setMiddleColor(new Color(0xCCCCFF));
	}

	static private ThreeColumnsLayoutManager m_layout;
	private WebMarkupContainer m_container;
	
	public BasePage()
	{
		this.add(new Label("page-title", "Swit - " + getPageTitle()));
		this.add(m_layout = new ThreeColumnsLayoutManager("layout", s_layout));
		m_layout.add(m_container = new TableImageBorder("outer-border", s_border, s_innerColor));
		
		
		ColumnPanel col = m_layout.getLeftColumn();
		col.setDefaultInlineCss(new Model<String>("text-align:center;"));
		
		Fragment frag = new Fragment(col.getContentId(), "imagefrag", this);
		frag.add(new Image("image", ButtonResource.getReference(), ButtonResource.getValueMap(s_logoTemplate, "SWIT")));
		col.addContent(frag);
		
		Menu menu = new Menu(col.getContentId());
		menu.setSelectedClass(this.getClass());
		col.addContent(menu);
		
	}

	protected String getPageTitle()
	{
		return "Home";
	}

	protected void innerAdd(Component... comps)
	{
		m_container.add(comps);
	}

	static protected Color getInnerColor()
	{
		return s_innerColor;
	}

	static protected Long getOuterBorder()
	{
		return s_border;
	}

	public void renderHead(IHeaderResponse response) 
	{
		response.renderCSSReference(new ResourceReference(BasePage.class, "BasePage.css"));
	}
	
}
