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
package net.kornr.swit.site.util;

import net.kornr.swit.button.ButtonResource;

import org.apache.wicket.IResourceListener;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.value.ValueMap;


public class MutableResourceReferenceLink<T> extends Link<T> implements IResourceListener 
{
	private ResourceReference m_resourceReference;
	private ValueMap m_resourceParameters;

	public MutableResourceReferenceLink(String id, ResourceReference ref, ValueMap params) 
	{
		super(id);
		m_resourceReference = ref;
		m_resourceParameters = params;
	}

	public void onResourceRequested() 
	{
		onClick();
	}

	@Override
	public void onClick() 
	{
	}
	
	@Override
	protected CharSequence getURL() 
	{
		if (m_resourceParameters != null)
			return RequestCycle.get().urlFor(m_resourceReference, m_resourceParameters);
		else
			return RequestCycle.get().urlFor(m_resourceReference);
	}

	public ResourceReference getResourceReference() {
		return m_resourceReference;
	}

	public void setResourceReference(ResourceReference resourceReference) {
		m_resourceReference = resourceReference;
	}

	public ValueMap getResourceParameters() {
		return m_resourceParameters;
	}

	public void setResourceParameters(ValueMap resourceParameters) {
		m_resourceParameters = resourceParameters;
	}

}
