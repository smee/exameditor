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
package net.kornr.swit.wicket.layout.threecol;

import java.awt.Color;
import java.util.HashMap;

import net.kornr.swit.util.LRUMap;
import net.kornr.swit.wicket.layout.LayoutInfo;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.value.ValueMap;


public class ThreeColumnsLayoutResource extends WebResource
{
	private static final long serialVersionUID = 1L;
	static private HashMap<String,LayoutInfo> m_layouts = new HashMap<String,LayoutInfo>();
	static private LRUMap<LayoutInfo, String> m_styles = new LRUMap<LayoutInfo, String>(10);
	
	static public ResourceReference getResourceReference()
	{
		return new ResourceReference(ThreeColumnsLayoutResource.class, "output") {
			@Override
			protected Resource newResource() {
				return new ThreeColumnsLayoutResource();
			}
		};
	}

	static public void install(WebApplication application, String mapping)
	{
		application.getSharedResources().add(ThreeColumnsLayoutResource.class, "output", null, null, new ThreeColumnsLayoutResource());
		ResourceReference ref = ThreeColumnsLayoutResource.getResourceReference();
		application.mountSharedResource(mapping, ref.getSharedResourceKey());
	}

	static public void register(LayoutInfo layout)
	{
		m_layouts.put(layout.getName(), layout);
	}

	static public String urlFor(LayoutInfo layout, Integer left, Integer right, Integer unit)
	{
		ResourceReference ref = ThreeColumnsLayoutResource.getResourceReference();
		ValueMap args = new ValueMap();
		args.put("id", layout.getName());
		if (left != null)
			args.put("left", left.toString());
		if (right != null)
			args.put("right", right.toString());
		if (unit != null)
		{
			switch(layout.getUnit())
			{
			case LayoutInfo.UNIT_EM:
				args.put("unit", "em");
				break;
			case LayoutInfo.UNIT_PIXEL:
				args.put("unit", "px");
				break;
			case LayoutInfo.UNIT_PERCENTAGE:
				args.put("unit", "%");
				break;
			}
		}
		
		return RequestCycle.get().urlFor(ref, args).toString();
	}
	
	private ThreeColumnsLayoutResource()
	{
	}

	static public String getStyle(LayoutInfo layout)
	{
		if (m_styles.containsKey(layout))
		{
			return m_styles.get(layout);
		}

		// Map<String,Object> map = new HashMap<String,Object>();
		ValueMap map = layout.getClassId();
		map.put("right-column-width", new Integer(layout.getRightSize()).toString());
		map.put("left-column-width", new Integer(layout.getLeftSize()).toString());
		map.put("right-plus-left-columns-width", Integer.toString(layout.getRightSize()+layout.getLeftSize()));
		map.put("hundred-minus-right-plus-left-pc", Integer.toString(100 - (layout.getRightSize()+layout.getLeftSize())));
		map.put("hundred-minus-left-pc", Integer.toString(100 - layout.getLeftSize()));
		
		map.put("left-color", LayoutInfo.toCssValue(layout.getLeftColor()));
		map.put("right-color", LayoutInfo.toCssValue(layout.getRightColor()));
		map.put("middle-color", LayoutInfo.toCssValue(layout.getMiddleColor()));

		String unit = "em";
		switch(layout.getUnit())
		{
		case LayoutInfo.UNIT_EM:
			unit = "em";
			break;
		case LayoutInfo.UNIT_PIXEL:
			unit ="px";
			break;
		case LayoutInfo.UNIT_PERCENTAGE:
			unit = "%";
			break;
		}
		
		map.put("unit", unit);
		
		PackagedTextTemplate template = null;
		
		String result = null;
		if (layout.getUnit() == LayoutInfo.UNIT_PERCENTAGE)
		{
			template = new PackagedTextTemplate(ThreeColumnsLayoutResource.class, "ThreeColumnsLayoutPanel_pc.css");
			result = template.asString(map);
		}
		else
		{
			template = new PackagedTextTemplate(ThreeColumnsLayoutResource.class, "ThreeColumnsLayoutPanel_empx.css");
			result = template.asString(map);
		}

		m_styles.put(layout, result);
		return result;
	}

	@Override
	public IResourceStream getResourceStream() 
	{
		ValueMap map = this.getParameters();
		String name = map.getString("id", null);
		if (name == null)
			return null;
		
		LayoutInfo current = m_layouts.get(name);
		current = current.duplicate();
		
		try {
			current.setLeftSize(map.getInt("left", current.getLeftSize()));
			current.setRightSize(map.getInt("right", current.getRightSize()));
			current.setUnit(map.getInt("unit", current.getUnit()));
			
			String leftcol = map.getString("leftcol", null);
			if (leftcol != null)
				current.setLeftColor(new Color(Integer.parseInt(leftcol)));

			String rightcol = map.getString("rightcol", null);
			if (rightcol!= null)
				current.setLeftColor(new Color(Integer.parseInt(rightcol)));

			String middlecol = map.getString("middlecol", null);
			if (middlecol!= null)
				current.setLeftColor(new Color(Integer.parseInt(middlecol)));

		} catch (Exception exc)
		{
			// We don't really care if there's an error
			exc.printStackTrace();
		}

		String css = getStyle(current);
		return new StringResourceStream(css, "text/css");
	}

//	static public void installSharedResource(final LayoutInfo layout, String mapping)
//	{
//		WebApplication.get().getSharedResources().add(layout.getName(), new ThreeColumnsLayoutResource(layout)); 
//		ResourceReference ref = new ResourceReference(layout.getName());
//		WebApplication.get().mountSharedResource(mapping, ref.getSharedResourceKey());
//	}
	
//	static public String createUniqueId(LayoutInfo layout)
//	{
//		return "3cols"+(s_counter++);
//	}
	
}
