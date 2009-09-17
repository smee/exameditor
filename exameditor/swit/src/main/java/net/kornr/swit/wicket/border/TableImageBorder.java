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
package net.kornr.swit.wicket.border;

import java.awt.Color;
import java.awt.Dimension;

import net.kornr.swit.util.StringUtils;
import net.kornr.swit.wicket.border.graphics.BorderMaker;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;


public class TableImageBorder extends Border 
{
	private Long m_roundedId;
	private WebMarkupContainer m_center;
	
	static public class ReplacementPicture
	{
		String type;
		String url;
		
		public ReplacementPicture(String type, String url)
		{
			this.type = type;
			this.url = url;
		}
	}

	static public class BorderImage extends ReplacementPicture
	{
		String cellStyle;
		
		public BorderImage(String type, String url, String cellStyle)
		{
			super(type,url);
			this.cellStyle = cellStyle;
		}
	}
	
	class StaticImage extends WebComponent 
	{
		String m_url;
		int m_width=-1,m_height=-1;
        public StaticImage(String id, IModel<String> model, int width, int height) {
            super(id, model);
            m_url = model.getObject();
            m_width=width;
            m_height=height;
        }

        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            checkComponentTag(tag, "img");
            tag.put("src", m_url);
            if (m_width>0)
            	tag.put("width", Integer.toString(m_width));
            if (m_height>0)
            	tag.put("height", Integer.toString(m_height));
        }
    }


	public TableImageBorder(String id, Long borderId, Color innerColor)
	{
		this(id, borderId, innerColor, null, null);
	}

	public TableImageBorder(String id, Long borderId, Color innerColor, ReplacementPicture[] pictures, BorderImage[] images) 
	{
		super(id);
		m_roundedId = borderId;
		
		BorderMaker bm = BorderMaker.get(m_roundedId);

		createAndAddCorner("tl", findReplacement("tl", pictures), bm);
		createAndAddCorner("tr", findReplacement("tr", pictures), bm);
		createAndAddCorner("bl", findReplacement("bl", pictures), bm);
		createAndAddCorner("br", findReplacement("br", pictures), bm);

		addBorder("top", BorderMaker.getUrl(borderId, "t", false), (BorderImage)findReplacement("t", images));
		addBorder("bottom", BorderMaker.getUrl(borderId, "b", false), (BorderImage)findReplacement("b", images));
		addBorder("left", BorderMaker.getUrl(borderId, "l", false), (BorderImage)findReplacement("l", images));
		addBorder("right", BorderMaker.getUrl(borderId, "r", false), (BorderImage)findReplacement("r", images));

		m_center = new WebMarkupContainer("center");
		if (innerColor != null)
			m_center.add(new AttributeAppender("style", new Model<String>("background-color:"+StringUtils.toHexColor(innerColor)+";"), ";"));
		this.add(m_center);
		
		m_center.add(getBodyContainer());
	}
	
	public void addCenterCellStyle(String cssStyle)
	{
		m_center.add(new AttributeAppender("style", true, new Model<String>(cssStyle), ";"));
	}
	
	private void addBorder(String id, String standardImgUrl, BorderImage replacement)
	{
		WebMarkupContainer container = new WebMarkupContainer(id);
		container.add(new AttributeAppender("style", new Model<String>("background-image:url('"+standardImgUrl+"');"), ";"));
		if (replacement == null)
		{
			container.add(new Image("inner_"+id).setVisible(false));
		}
		else
		{
			container.add(new StaticImage("inner_"+id, new Model<String>(replacement.url), -1,-1));
			container.add(new AttributeAppender("style", new Model<String>(replacement.cellStyle), ";"));
		}
		this.add(container);
	}
	
	private String getCssForDimensionOf(BorderMaker bm, String part)
	{
		Dimension dim = bm.getMapSize(part);
		return "width:"+dim.width+"px;height:"+dim.height+"px;";
	}
	
	public ReplacementPicture findReplacement(String type, ReplacementPicture[] pics)
	{
		if (pics == null)
			return null;
		
		for (ReplacementPicture r: pics)
		{
			if (type.equalsIgnoreCase(r.type))
			{
				return r;
			}
		}
		return null;
	}

	
	private WebMarkupContainer createAndAddCorner(String name, ReplacementPicture replacement, BorderMaker bm)
	{
		String style = null;
		Dimension dim = bm.getMapSize(name);
		style = getCssForDimensionOf(bm, name);
		WebComponent image = null;
		if (replacement == null)
		{
			image = BorderMaker.getImage("img_"+name, m_roundedId, name, false);
		}
		else
		{
			image = new StaticImage("img_"+name, new Model<String>(replacement.url), dim.width, dim.height);
		}
		
		WebMarkupContainer wmc = new WebMarkupContainer(name);
		this.add(wmc);
		if (style != null)
			wmc.add(new AttributeAppender("style", new Model<String>(style), ";"));
		
		wmc.add(image);
		return wmc;
	}

}
