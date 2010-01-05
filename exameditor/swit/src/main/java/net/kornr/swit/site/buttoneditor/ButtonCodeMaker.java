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

import java.awt.Font;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import net.kornr.swit.button.ButtonResource;
import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.site.widget.FontSelectorPanel;
import net.kornr.swit.site.widget.IntegerField;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ImageButton;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;


public class ButtonCodeMaker implements Serializable
{
	private List<ButtonProperty> m_properties = new LinkedList<ButtonProperty>();
	private List<ButtonProperty> m_classProperties = new LinkedList<ButtonProperty>();
	private ButtonDescriptor m_descriptor;
	private List<String> m_effects = new LinkedList<String>();
	private IModel<String> m_textModel;

	public ButtonCodeMaker(ButtonDescriptor descriptor, List<ButtonProperty> props, IModel<String> textModel)
	{
		m_descriptor = descriptor;
		m_properties = props;
		m_textModel = textModel;
	}
	
	public String getCode()
	{
		String text = m_textModel.getObject();
		if (text == null)
			text = "";
		text = text.replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t");

		StringBuffer buffer = new StringBuffer();
		String clzz = m_descriptor.getTemplateClass();
		this.addLine(buffer, 0, clzz+" template = new " + clzz +"();");

		for (ButtonProperty p: m_properties)
			addLine(buffer, 0, "template." + getSetter(p)+"(" + getJavaObject(p) + ");");

		for (ButtonProperty p: m_classProperties)
			addLine(buffer, 0, "template." + getSetter(p)+"(" + getJavaObject(p) + ");");

		for (String eff : m_effects)
			addLine(buffer, 0, "template.addEffect(" + eff + ");");
		
		addLine(buffer, 0, "");
		addLine(buffer, 0, "// Then to create a Wicket image:");
		addLine(buffer, 0, "Image myImage = new Image(\"id\", ButtonResource.getReference(), ButtonResource.getValueMap(template, \""+text+"\"));");
		addLine(buffer, 0, "// Or an ImageButton:");
		addLine(buffer, 0, "ImageButton myImageButton = new ImageButton(\"id\", ButtonResource.getReference(), ButtonResource.getValueMap(template, \""+text+"\"));");
		return buffer.toString();
	}
	
	private String getSetter(ButtonProperty prop)
	{
		return "set" + prop.getPropertyName().substring(0,1).toUpperCase()+prop.getPropertyName().substring(1);
	}
	
	private String getJavaObject(ButtonProperty prop)
	{
		if (prop.getValue() == null)
			return "null";
		
		if (prop.getType().equals(ButtonProperty.Type.TYPE_STRING))
		{
			return "\""+prop.getValue().toString()+"\"";
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_COLOR))
		{
			String s = prop.getValue().toString();
			if (s.startsWith("#"))
				s = s.substring(1);
			
			return "new Color(0x" + s + ")";
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_BOOLEAN))
		{
			return ((Boolean)prop.getValue())?"Boolean.TRUE":"Boolean.FALSE";
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_FONT))
		{
			Font f = (Font)prop.getValue();
			return "new Font(\"" + f.getFamily(Locale.US) + "\", " + this.getFontStyles(f) + ", " + f.getSize()+")";
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_INTEGER))
		{
			return prop.getValue().toString();
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_FLOAT))
		{
			return prop.getValue().toString()+"f";
		}
		
		return "\"Invalid Value\"";
	}
	
	private String getFontStyles(Font f)
	{
		StringBuffer buffer = new StringBuffer();
		int style = f.getStyle();
		if ((style & Font.BOLD)!=0)
			buffer.append((buffer.length()>0?"|":"") + "Font.BOLD");
		if ((style & Font.ITALIC)!=0)
			buffer.append((buffer.length()>0?"|":"") + "Font.ITALIC");
		
		if (buffer.length() == 0)
			buffer.append("Font.PLAIN");
		
		return buffer.toString();
	}
	
	public void addLine(StringBuffer buffer, int indentationLevel, String line)
	{
		for (int i=0; i<indentationLevel; i++)
			buffer.append("\t");
		buffer.append(line);
		buffer.append("\n");
	}

	public List<ButtonProperty> getProperties() {
		return m_properties;
	}

	public void setProperties(List<ButtonProperty> properties) {
		m_properties = properties;
	}

	public ButtonDescriptor getDescriptor() {
		return m_descriptor;
	}

	public void setDescriptor(ButtonDescriptor descriptor) {
		m_descriptor = descriptor;
	}

	public List<String> getEffects() {
		return m_effects;
	}

	public void setEffects(List<String> effects) {
		m_effects = effects;
	}

	public List<ButtonProperty> getClassProperties() {
		return m_classProperties;
	}

	public void setClassProperties(List<ButtonProperty> classProperties) {
		m_classProperties = classProperties;
	}

}
