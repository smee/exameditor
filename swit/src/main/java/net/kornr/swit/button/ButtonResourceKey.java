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
package net.kornr.swit.button;

import java.io.Serializable;

class ButtonResourceKey implements Serializable 
{
	private ButtonTemplate m_template;
	private String m_text;
	private Long m_id;
	
	public ButtonResourceKey(ButtonTemplate template, String text)
	{
		m_template = template;
		m_text = text;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ButtonResourceKey)
		{
			ButtonResourceKey other = (ButtonResourceKey)obj;
			return other.m_text.equals(this.m_text) && other.m_template.equals(this.m_template);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return m_template.hashCode() * m_text.hashCode();
	}

	public ButtonTemplate getTemplate() {
		return m_template;
	}
	
	public String getText() {
		return m_text;
	}
	
	public Long getId() {
		return m_id;
	}
	
	public void setId(Long id) {
		m_id = id;
	}
	
	public void setTemplate(ButtonTemplate template) {
		m_template = template;
	}
	
	public void setText(String text) {
		m_text = text;
	}

}
