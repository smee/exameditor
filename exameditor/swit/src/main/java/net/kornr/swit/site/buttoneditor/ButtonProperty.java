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

import java.io.Serializable;

public class ButtonProperty implements Serializable 
{
	enum Type {
		TYPE_STRING,
		TYPE_COLOR,
		TYPE_BOOLEAN,
		TYPE_FONT,
		TYPE_FLOAT,
		TYPE_INTEGER,
		TYPE_TEXT_TRANSFORM,
		TYPE_TEXT_ALIGN
	};
	
	private String m_propertyName;
	private String m_label;
	private Object m_value;
	private Type m_type;
	
	public ButtonProperty() { } 
	
	public ButtonProperty(String label, String propertyName, Object value, Type type)
	{
		m_label = label;
		m_propertyName = propertyName;
		m_value = value;
		m_type = type;
	}

	public String getPropertyName() {
		return m_propertyName;
	}

	public void setPropertyName(String propertyName) {
		m_propertyName = propertyName;
	}

	public String getLabel() {
		return m_label;
	}

	public void setLabel(String label) {
		m_label = label;
	}

	public Object getValue() {
		return m_value;
	}

	public void setValue(Object value) {
		m_value = value;
	}

	public Type getType() {
		return m_type;
	}

	public void setType(Type type) {
		m_type = type;
	}

}
