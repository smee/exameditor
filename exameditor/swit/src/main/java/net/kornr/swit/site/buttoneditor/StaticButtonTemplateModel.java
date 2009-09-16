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

import java.util.HashMap;

import net.kornr.swit.button.ButtonTemplate;

import org.apache.wicket.model.IModel;


public class StaticButtonTemplateModel implements IModel<ButtonTemplate> 
{
	private static final long serialVersionUID = 1L;
	static private HashMap<Long, ButtonTemplate> s_templates = new HashMap<Long, ButtonTemplate>();
	static private long s_counter = 0;
	
	synchronized static public Long register(final ButtonTemplate template)
	{
		long id = s_counter++;
		s_templates.put(id, template);
		return id;
	}
	
	private Long m_id;
	
	public StaticButtonTemplateModel(Long id)
	{
		m_id = id;
	}
	
	public ButtonTemplate getObject() {
		return s_templates.get(m_id);
	}

	public void setObject(ButtonTemplate templ) {
		s_templates.put(m_id, templ);
	}

	public void detach() {
	}
}
