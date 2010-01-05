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
package net.kornr.swit.site.widget;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.kornr.swit.button.ButtonResource;
import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.button.NothingButTextButton;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;


public class FontSelectorPanel extends FormComponentPanel<Font> 
{
	private IModel<Font> m_model;
	
	private static String[] s_fonts = new String[] {
		"Arial", "Verdana", "Courier"
	};
	private static List<String> s_fontsList = Arrays.asList(s_fonts);
	static private HashMap<String, ButtonTemplate> s_templates = new HashMap<String, ButtonTemplate>();
	private String m_fontName = s_fonts[0];
	private int m_fontStyle = Font.PLAIN;
	private int m_fontSize = 14;
	private boolean m_bold = true;
	private boolean m_italic = false;
	
	public FontSelectorPanel(String id, IModel<Font> model) {
		super(id, model);
		m_model = model;
		
		Font f = model.getObject();
		if (f != null)
		{
			m_fontName = f.getFontName();
			m_fontSize = f.getSize();
			m_fontStyle = f.getStyle();
			m_bold = (f.getStyle()&Font.BOLD)!=0;
			m_italic = (f.getStyle()&Font.ITALIC)!=0;
		}
		init();
	}

	private void init()
	{
		RadioGroup fontgroup = new RadioGroup("fontgroup", new PropertyModel<String>(this, "fontName"));
		this.add(fontgroup);
		fontgroup.add(new ListView<String>("fontitem", s_fontsList) {
			@Override
			protected void populateItem(ListItem<String> item) 
			{
				String font = item.getModelObject();
				item.add(new Radio("radio", new Model<String>(font)));
				ButtonTemplate tmpl = s_templates.get(font);
				if (tmpl == null)
				{
					tmpl = new NothingButTextButton();
					tmpl.setFont(new Font(font, Font.PLAIN, 24));
					tmpl.setFontColor(Color.black);
					tmpl.setHeight(1);
					tmpl.setWidth(1);
					s_templates.put(font, tmpl);
				}
				
				item.add(new Image("sample", ButtonResource.getReference(), ButtonResource.getValueMap(tmpl, font)));
			}
		});
		
		// this.add(new TextField("size", new PropertyModel<Integer>(this, "fontSize")));
		this.add(new IntegerField("size", new IntegerField.IntegerAdaptor(new PropertyModel<Integer>(this, "fontSize")),1, false, 6d, 144d));
		this.add(new CheckBox("bold", new PropertyModel<Boolean>(this, "bold")));
		this.add(new CheckBox("italic", new PropertyModel<Boolean>(this, "italic")));
	}
	
	@Override
	protected void convertInput() 
	{
		try {
			m_fontStyle = 0;
			if (m_bold || m_italic)
			{
				m_fontStyle = 0;
				m_fontStyle |= m_bold?Font.BOLD:0;
				m_fontStyle |= m_italic?Font.ITALIC:0;
			}
			else
			{
				m_fontStyle = Font.PLAIN;
			}
			Font font = new Font(m_fontName, m_fontStyle, m_fontSize);
			this.setConvertedInput(font);
		} catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}

	@Override
	public void updateModel() {
		convertInput();
		Font f = this.getConvertedInput();
		this.setModelObject(f);
	}

	public String getFontName() {
		return m_fontName;
	}

	public void setFontName(String fontName) {
		m_fontName = fontName;
	}

	public int getFontStyle() {
		return m_fontStyle;
	}

	public void setFontStyle(int fontStyle) {
		m_fontStyle = fontStyle;
	}

	public int getFontSize() {
		return m_fontSize;
	}

	public void setFontSize(int fontSize) {
		m_fontSize = fontSize;
	}

	public boolean isBold() {
		return m_bold;
	}

	public void setBold(boolean bold) {
		m_bold = bold;
	}

	public boolean isItalic() {
		return m_italic;
	}

	public void setItalic(boolean italic) {
		m_italic = italic;
	}

	
}
