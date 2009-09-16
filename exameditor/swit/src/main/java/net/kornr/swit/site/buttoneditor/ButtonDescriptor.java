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

import java.awt.Color;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import net.kornr.swit.button.ButtonTemplate;

import org.apache.commons.beanutils.BeanUtils;


public class ButtonDescriptor implements Serializable 
{
	private String m_name;
	private String m_description;
	private String m_templateClass;
	private List<ButtonProperty> m_properties;

	public ButtonDescriptor(String name, String description, String clzz, ButtonProperty[] props)
	{
		this(name, description, clzz, Arrays.asList(props));
	}
	
	public ButtonDescriptor(String name, String description, String clzz, List<ButtonProperty> props)
	{
		m_name = name;
		m_description = description;
		m_templateClass = clzz;
		m_properties = props;
	}
	
	public String getName() {
		return m_name;
	}
	public void setName(String name) {
		m_name = name;
	}
	public String getDescription() {
		return m_description;
	}
	public void setDescription(String description) {
		m_description = description;
	}

	public ButtonTemplate createTemplate()
	{
		try {
			return (ButtonTemplate) Class.forName(m_templateClass).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public List<ButtonProperty> getProperties()
	{
		return m_properties;
	}
	
	public void applyProperties(ButtonTemplate template, List<ButtonProperty> props) throws Exception
	{
		for (ButtonProperty p : props)
		{
			BeanUtils.setProperty(template, p.getPropertyName(), p.getValue());
		}	
	}
	
	public void applyProperty(ButtonTemplate template, ButtonProperty prop) throws Exception
	{
		BeanUtils.setProperty(template, prop.getPropertyName(), prop.getValue());
	}

	public String getTemplateClass() {
		return m_templateClass;
	}

	public void setTemplateClass(String templateClass) {
		m_templateClass = templateClass;
	}

	public void setProperties(List<ButtonProperty> properties) {
		m_properties = properties;
	}

}
