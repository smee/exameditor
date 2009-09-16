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

import net.kornr.swit.site.jquery.colorpicker.ColorPickerField;
import net.kornr.swit.site.widget.FontSelectorPanel;
import net.kornr.swit.site.widget.IntegerField;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;


public class ButtonPropertyEditorPanel extends Panel 
{
	private ButtonProperty m_property;
	
	public ButtonPropertyEditorPanel(String id, ButtonProperty property, boolean displayLabel) 
	{
		super(id);
		m_property = property;
		Fragment frag = createEditor("editor", property, displayLabel);
		this.add(frag);
	}

	public Fragment createEditor(String id, ButtonProperty prop, boolean displayLabel)
	{
		Fragment frag = null;
		String label = prop.getLabel();
		if (prop.getType().equals(ButtonProperty.Type.TYPE_STRING))
		{
			frag = new Fragment(id, "textfield", this);
			frag.add(new TextField<String>("prop", new PropertyModel<String>(prop, "value")));
			label+=":";
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_COLOR))
		{
			frag = new Fragment(id, "textfield", this);
			ColorPickerField field = new ColorPickerField("prop", new PropertyModel<String>(prop, "value"), null);
			frag.add(field);
			label+=":";
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_BOOLEAN))
		{
			frag = new Fragment(id, "checkbox", this);
			frag.add(new CheckBox("prop", new PropertyModel<Boolean>(prop, "value")));
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_FONT))
		{
			frag = new Fragment(id, "div", this);
			frag.add(new FontSelectorPanel("prop", new PropertyModel<Font>(prop, "value")));
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_INTEGER))
		{
			frag = new Fragment(id, "div", this);
			frag.add(new IntegerField("prop", new IntegerField.IntegerAdaptor(new PropertyModel<Integer>(prop, "value")), 1));
			label+=":";
		}
		else if (prop.getType().equals(ButtonProperty.Type.TYPE_FLOAT))
		{
			frag = new Fragment(id, "div", this);
			frag.add(new IntegerField("prop", new IntegerField.FloatAdaptor(new PropertyModel<Float>(prop, "value")), 1f));
			label+=":";
		}
		else
		{
			frag = new Fragment(id, "div", this);
			frag.setVisible(false);
		}

		frag.add(new Label("name", label).setVisible(displayLabel));
		return frag;
	}

}
