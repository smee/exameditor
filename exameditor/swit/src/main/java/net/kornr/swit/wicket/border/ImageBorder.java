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

import java.util.HashMap;

import net.kornr.swit.util.MappedString;
import net.kornr.swit.wicket.border.graphics.BorderMaker;
import net.kornr.swit.wicket.border.graphics.SizedBorder;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.util.string.CssUtils;
import org.apache.wicket.util.template.PackagedTextTemplate;

/**
 * ImageBorder implements a pure-css liquid border built with images, suitable for rounded borders for instance. 
 * Note however that css-based liquid borders are significantly harder to use than the TableImageBorder, because of the many constraints
 * of CSS. So, unless you really need to avoid tables, for technical or ideological reasons, we recommend using TableImageBorder whenever possible.
 * 
 * <p>
 * Before using this class, you should be familiar with the liquid rounded-corner CSS technique, please check the web for tutorials.
 * Basically, it relies on images parts overlapping each other, and has the following constraints over the images used:
 * <ul>
 * 	<li>The external color of the border (outside the border) must NOT be transparent. This is a major drawback, as you need to make the 
 * component aware of the background (unlike the TableImageBorder, which can use a transparent background color)</li>
 *  <li>The maximum size of the display must be known in advance. Yes, that's make liquid css mildly liquid, really, but that's the constraint of this CSS technique.
 * </ul>
 * 
 * As a consequence of those constraints, here is what you must do on your code to make it work smoothly:
 * <ul>
 * 	 <li>The graphics of the border must have a non-transparent background, and have a size as big as possible. To do that, you just need to apply the following
 * border objects to your initial border:
 * <pre>
 * 	  static private Long FORCESIZE = SizedBorder.register(YOURBORDER, 1200, 800);
 * 	  static private Long FORCEBACKGROUND = MarginBorder.register(s_big, 0, 0, 0, 0, BACKGROUNDCOLOR);
 * </pre>
 * This transform your original <tt>YOURBORDER</tt> border into something suitable for the css liquid layout. Note the width and height of the newly created border, 
 * 1200x800, this is the maximum size your liquid layout should be able to reach.
 * <p> To create an ImageTable object, just use <tt>ImageTable.createFromBorder("someId", FORCEBACKGROUND)</tt> (note that you must use the FORCEBACKGROUND border: 
 * the <tt>YOURBORDER</tt> border is not modified, you must use the result of the chain of transformation that ends with the <tt>FORCEBACKGROUND</tt> object. 
 * </li>
 * <li>
 *  	To ensure your layout doesn't get bigger than your maximum, specify a <tt>style="max-width:1200px; max-height:800px"</tt> on your html markup. 
 * </li>
 * </ul>
 * 
 */
public class ImageBorder extends Border implements IHeaderContributor
{
	private WebMarkupContainer m_roundBoxTop, m_roundBoxTopRight, m_roundBoxTopLeft, m_roundBoxBottomRight, m_roundBoxBottomLeft;
	private WebMarkupContainer m_content;
	
	private String  m_topRightImg, m_topLeftImg, m_bottomRightImg, m_bottomLeftImg;
	
	static private MappedString s_css = // new MappedString("#${0} { margin:0; padding:0; background:url('${1}') white repeat-x left top; }\n"
			new MappedString(""
			+ "#${2} { 	 margin:0; padding:0; background:url('${3}') white no-repeat right top; }\n"
			+ "#${4} { margin:0; padding:0; background: url('${5}') no-repeat left top; }\n"
			+ "#${6} { margin:0; padding:0; background: url('${7}') no-repeat right bottom; }\n"
			+ "#${8} { margin:0; padding:0; background: url('${9}') no-repeat left bottom; }\n"
			+ "#${10} { margin:0; padding: 0; }");

	/**
	 * Use this static factory method to create an ImageBorder using a borderId. Please refer to the documentation of the class
	 * for the constraints.
	 * @param id the wicket component id
	 * @param borderId an id for the border
	 * @return an ImageBorder border
	 */
	public static ImageBorder createFromBorder(String id, Long borderId)
	{
		BorderMaker border = BorderMaker.get(borderId);
		String tr = border.getUrl(borderId, "cell2356", false);
		String tl = border.getUrl(borderId, "cell14", false);
		String br = border.getUrl(borderId, "cell89", false);
		String bl = border.getUrl(borderId, "bl", false);
		return new ImageBorder(id, tr, tl, br, bl);
	}
	
	/**
	 * Creates an image border with the four image URL provided as string 
	 * @param id id of the wicket component
	 * @param topRightImg the top right image url
	 * @param topLeftImg  the top left image url
	 * @param bottomRightImg the bottom right image url
	 * @param bottomLeftImg the bottom left image url
	 */
	public ImageBorder(String id, String topRightImg, String topLeftImg, String bottomRightImg, String bottomLeftImg)
	{
		super(id);
		m_topRightImg = topRightImg;
		m_topLeftImg = topLeftImg;
		m_bottomRightImg = bottomRightImg;
		m_bottomLeftImg = bottomLeftImg;
		init();		
	}
	
	/**
	 * Creates an image border using the four images provided as ResourceReference objects.
	 * @param id id of the wicket component
	 * @param topRightImg the top right image 
	 * @param topLeftImg  the top left image 
	 * @param bottomRightImg the bottom right image 
	 * @param bottomLeftImg the bottom left image
	 */
	public ImageBorder(String id, ResourceReference topRightImg, ResourceReference topLeftImg, ResourceReference bottomRightImg, ResourceReference bottomLeftImg) 
	{
		super(id);
		m_topRightImg = this.urlFor(topRightImg).toString();
		m_topLeftImg = this.urlFor(topLeftImg).toString();
		m_bottomRightImg = this.urlFor(bottomRightImg).toString();
		m_bottomLeftImg = this.urlFor(bottomLeftImg).toString();
		init();
	}
	
	public void init()
	{
		m_roundBoxTop = new WebMarkupContainer("roundbox-top");
		m_roundBoxTopRight = new WebMarkupContainer("roundbox-tr");
		m_roundBoxTopLeft = new WebMarkupContainer("roundbox-tl");
		m_roundBoxBottomRight = new WebMarkupContainer("roundbox-br");
		m_roundBoxBottomLeft = new WebMarkupContainer("roundbox-bl");
		m_content = new WebMarkupContainer("roundbox-content");

		m_roundBoxTop.setOutputMarkupId(true);
		m_roundBoxTopRight.setOutputMarkupId(true);
		m_roundBoxTopLeft.setOutputMarkupId(true);
		m_roundBoxBottomRight.setOutputMarkupId(true);
		m_roundBoxBottomLeft.setOutputMarkupId(true);
		m_content.setOutputMarkupId(true);
		
		m_roundBoxTop.add(m_roundBoxTopRight);
		m_roundBoxTopRight.add(m_roundBoxTopLeft);
		m_roundBoxTopLeft.add(m_roundBoxBottomRight);
		m_roundBoxBottomRight.add(m_roundBoxBottomLeft);
		m_roundBoxBottomLeft.add(m_content);

		m_content.add(getBodyContainer());
		this.add(m_roundBoxTop);
	}

	public void renderHead(IHeaderResponse response) 
	{
		String css = CssUtils.INLINE_OPEN_TAG
			+ s_css.map(
					"", "",
					m_roundBoxTopRight.getMarkupId(), m_topRightImg,
					m_roundBoxTopLeft.getMarkupId(), m_topLeftImg,
					m_roundBoxBottomRight.getMarkupId(), m_bottomRightImg,
					m_roundBoxBottomLeft.getMarkupId(), m_bottomLeftImg,
					m_content.getMarkupId()
					)
			+ CssUtils.INLINE_CLOSE_TAG;
		response.renderString(css);
	}

}
