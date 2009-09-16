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
package net.kornr.swit.site.quickstart;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

import net.kornr.swit.site.WicketApplication;
import net.kornr.swit.site.jquery.JQuery;
import net.kornr.swit.site.jquery.JQueryTabs;
import net.kornr.swit.site.jquery.tools.JQueryTools;
import net.kornr.swit.site.util.shjs.Shjs;
import net.kornr.swit.util.StringUtils;
import net.kornr.swit.wicket.border.ImageBorder;
import net.kornr.swit.wicket.border.TableImageBorder;
import net.kornr.swit.wicket.border.graphics.RoundedBorderMaker;
import net.kornr.swit.wicket.border.graphics.SizedBorder;
import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.InternalFrame;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.locator.ResourceStreamLocator;

public class ExampleDisplay extends Panel implements IHeaderContributor
{
	static private Color s_bgcolor = new Color(0xdaeaef);
	static private Long s_border = RoundedBorderMaker.register(20, 6, new Color(0xFFDD88), s_bgcolor);
	static private Long s_big = SizedBorder.register(s_border, 1200, 800);
	static private Long s_margin = net.kornr.swit.wicket.border.graphics.MarginBorder.register(s_big, 0, 0, 0, 0, s_bgcolor);

	private WebMarkupContainer m_java, m_html, m_result; 
	private JQueryTabs m_tabs = new JQueryTabs();
	
	public ExampleDisplay(String id, IModel<Class> model) {
		super(id, model);

		TableImageBorder border = new TableImageBorder("border", s_border, s_bgcolor);
		this.add(border);
		
		border.add(new Label("classname", model.getObject().getCanonicalName()));
		
		m_java = new WebMarkupContainer("java");
		m_html = new WebMarkupContainer("html");
		m_result = new WebMarkupContainer("result");
		
		border.add(m_java, m_html, m_result);
		ExternalLink javalink;
		ExternalLink htmllink;
		ExternalLink resultlink;

		javalink = new ExternalLink("linkjava", "#");
		htmllink = new ExternalLink("linkhtml", "#");
		resultlink = new ExternalLink("linkresult", "#");
		border.add(javalink, htmllink, resultlink);
		
		m_tabs.add(javalink, m_java);
		m_tabs.add(htmllink, m_html);
		m_tabs.add(resultlink, m_result);

		m_java.add(SourcePage.createIFrame("source", model.getObject(), "java"));
		m_html.add(SourcePage.createIFrame("html", model.getObject(), "html"));
		
		try {
			Constructor constructor = model.getObject().getConstructor(String.class);
			Component comp = (Component) constructor.newInstance("instance");
			m_result.add(comp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_result.add(new Label("instance", "Impossible to instanciate " + model.getObject().getCanonicalName()));
		}
	}

	public void renderHead(IHeaderResponse response) {
		response.renderJavascriptReference(JQuery.getReference());
		response.renderJavascriptReference(JQueryTools.getReference());
		m_tabs.configure(response);
	}

}
