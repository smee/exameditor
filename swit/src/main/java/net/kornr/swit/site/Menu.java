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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.kornr.swit.button.AmazonianButton;
import net.kornr.swit.button.ButtonResource;
import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.button.effect.AutoClip;
import net.kornr.swit.button.effect.ShadowBorder;
import net.kornr.swit.site.buttoneditor.ButtonEditor;
import net.kornr.swit.site.jquery.JQuery;
import net.kornr.swit.site.quickstart.QuickStart;
import net.kornr.swit.util.MappedString;
import net.kornr.swit.util.Pair;
import net.kornr.swit.wicket.border.graphics.GfxEffects;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.value.ValueMap;


public class Menu extends Panel implements IHeaderContributor
{
	static AmazonianButton s_button = new AmazonianButton();
	static AmazonianButton s_button_hover = new AmazonianButton();
	static AmazonianButton s_button_selected = new AmazonianButton();
	
	static {
		s_button.setFont(new Font("Verdana", Font.BOLD, 18));
		s_button.setWidth(225);
		s_button.setHeight(24);
		s_button.setRightHanded(true);
		s_button.setIcon(null);
		s_button.setFontColor(new Color(0xDDDDDD));
		s_button.setAutoExtend(Boolean.TRUE);
		s_button.setShadowDisplayed(Boolean.TRUE);
		s_button.addEffect(new ShadowBorder(4,0,0,Color.black)).addEffect(new AutoClip());
		s_button.setInnerColor(new Color(0x3a7ab3));
		s_button.setOuterColor(new Color(0x5263a8));

		s_button_hover.setFont(new Font("Verdana", Font.BOLD, 18));
		s_button_hover.setWidth(225);
		s_button_hover.setHeight(24);
		s_button_hover.setRightHanded(true);
		s_button_hover.setIcon(null);
		s_button_hover.setFontColor(new Color(0xFFFFFF));
		s_button_hover.setAutoExtend(Boolean.TRUE);
		s_button_hover.setShadowDisplayed(Boolean.TRUE);
		s_button_hover.addEffect(new ShadowBorder(4,0,0,Color.black)).addEffect(new AutoClip());
		s_button_hover.setInnerColor(GfxEffects.adjustBrightness(new Color(0x3a7ab3), 1.5f));
		s_button_hover.setOuterColor(GfxEffects.adjustBrightness(new Color(0x5263a8), 1.5f));

		s_button_selected.setFont(new Font("Verdana", Font.BOLD, 18));
		s_button_selected.setWidth(225);
		s_button_selected.setHeight(24);
		s_button_selected.setFontColor(new Color(0xFFFFFF));
		s_button_selected.setAutoExtend(Boolean.TRUE);
		s_button_selected.setShadowDisplayed(Boolean.TRUE);
		s_button_selected.addEffect(new ShadowBorder(4,0,0,Color.black)).addEffect(new AutoClip());
		s_button_selected.setInnerColor(GfxEffects.adjustBrightness(new Color(0x3a7ab3), 1.7f));
		s_button_selected.setOuterColor(GfxEffects.adjustBrightness(new Color(0x5263a8), 1.7f));
		s_button_selected.setRightHanded(true);		
	}
	
	static private List<Pair> s_menus = Arrays.asList(new Pair[] {
		new Pair<String,Class>("Home", HomePage.class),
		new Pair<String,Class>("Button Editor", ButtonEditor.class),
		new Pair<String,Class>("QuickStart", QuickStart.class),
		new Pair<String,Class>("Download", Download.class)
	});
	
	private HashMap<String, String> m_ids = new HashMap<String, String>();
	
	private Class m_selectedClass = null;
	
	public Menu(String id)
	{
		super(id);

		this.add(new ListView<Pair>("items", s_menus) {
			@Override
			protected void populateItem(ListItem<Pair> item) {
				Pair<String,Class> p = item.getModelObject();
				
				BookmarkablePageLink link = new BookmarkablePageLink("link", p.getSecond());
				item.add(link);

				Image img;
				if (p.getSecond().equals(m_selectedClass))
				{
					link.add(img = ButtonResource.getImage("img", s_button_selected, p.getFirst()));
					link.setEnabled(false);
				}
				else
					link.add(img = ButtonResource.getImage("img", s_button, p.getFirst()));
				img.setOutputMarkupId(true);
				m_ids.put(p.getFirst(), img.getMarkupId());
			}
			
		});
	}

	public void renderHead(IHeaderResponse response) 
	{
		response.renderJavascriptReference(JQuery.getReference());
		
		MappedString script = new MappedString("$('#${0}').hover(function(){$(this).attr('src', '${2}');}, function(){$(this).attr('src', '${1}');});");
		for (Pair<String,Class> p: s_menus)
		{
			if (p.getSecond().equals(m_selectedClass) == false)
			{
				String url = RequestCycle.get().urlFor(ButtonResource.getReference(), ButtonResource.getValueMap(s_button, p.getFirst())).toString(); 
				String hoverurl= RequestCycle.get().urlFor(ButtonResource.getReference(), ButtonResource.getValueMap(s_button_hover, p.getFirst())).toString(); 
				
				response.renderJavascript(JQuery.getOnReadyScript(script.map(m_ids.get(p.getFirst()), url, hoverurl)), null);
			}
		}		
	}

	public Class getSelectedClass() {
		return m_selectedClass;
	}

	public void setSelectedClass(Class selectedClass) {
		m_selectedClass = selectedClass;
	}
	
}
