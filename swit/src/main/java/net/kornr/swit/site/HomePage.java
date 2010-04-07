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
package net.kornr.swit.site;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import net.kornr.swit.button.AmazonianButton;
import net.kornr.swit.button.ButtonResource;
import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.button.SoftShiningButton;
import net.kornr.swit.button.VistafarianButton;
import net.kornr.swit.button.WebTwoButton;
import net.kornr.swit.button.effect.AutoClip;
import net.kornr.swit.button.effect.Rotate;
import net.kornr.swit.button.effect.ShadowBorder;
import net.kornr.swit.button.effect.VerticalMirror;
import net.kornr.swit.site.buttoneditor.ButtonEditor;
import net.kornr.swit.site.jquery.JQuery;
import net.kornr.swit.site.jquery.tools.JQueryTools;
import net.kornr.swit.wicket.border.ImageBorder;
import net.kornr.swit.wicket.border.TableImageBorder;
import net.kornr.swit.wicket.border.graphics.GenericShadowBorder;
import net.kornr.swit.wicket.border.graphics.GfxEffects;
import net.kornr.swit.wicket.border.graphics.GlossyRoundedBorderMaker;
import net.kornr.swit.wicket.border.graphics.MarginBorder;
import net.kornr.swit.wicket.border.graphics.RoundedBorderMaker;
import net.kornr.swit.wicket.border.graphics.SizedBorder;
import net.kornr.swit.wicket.layout.ColumnPanel;
import net.kornr.swit.wicket.layout.LayoutInfo;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutBorderFixed;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutBorderPc;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutResource;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;


/**
 * Homepage
 */
public class HomePage extends BasePage implements IHeaderContributor
{
	private static final long serialVersionUID = 1L;

	static private WebTwoButton s_disclaimerButton = new WebTwoButton();
	
	//
	// We use a lot of buttons on this page
	//
	
	static private ButtonTemplate s_button1 = new VistafarianButton();
	static private AmazonianButton s_button2 = new AmazonianButton();
	static private ButtonTemplate s_button3 = new WebTwoButton();
	static private ButtonTemplate s_button4 = new SoftShiningButton(); 
	static private VistafarianButton s_button5 = new net.kornr.swit.button.VistafarianButton();
	static private WebTwoButton s_button6 = new WebTwoButton();
	
	static {
		s_disclaimerButton.setFont(new Font("Monospaced", Font.BOLD, 18));
		s_disclaimerButton.setShadowDisplayed(Boolean.TRUE);
		s_disclaimerButton.setBaseColor(new Color(0x961313));
		s_disclaimerButton.setTextTransform(ButtonTemplate.TEXT_TRANSFORM_ALLCAPS);
		
		s_button1.setFont(new Font("Arial", Font.BOLD|Font.ITALIC, 48));
		s_button2.setFont(new Font("Courier", Font.BOLD|Font.ITALIC, 26));
		s_button3.setFont(new Font("Verdana", 0, 32));
		s_button4.setFont(new Font("Serif", 0, 24));
		s_button2.setRightHanded(true);
		s_button1.setShadowDisplayed(true);
		s_button3.setShadowDisplayed(true);
		s_button4.setShadowDisplayed(true);
		s_button1.addEffect(new ShadowBorder(6, 2, 2, Color.black)).addEffect(new AutoClip());
		s_button2.addEffect(new ShadowBorder(4, 0, 0, new Color(0x666633))).addEffect(new AutoClip());
		s_button3.addEffect(new VerticalMirror(0.6f, 0.6f)).addEffect(new AutoClip());
		s_button4.addEffect(new ShadowBorder(12, 0, 0, new Color(0xAABB00))).addEffect(new AutoClip());
		s_button4.addEffect(new Rotate(Math.PI/24)).addEffect(new AutoClip());
		s_button4.setFontColor(Color.blue);
		
		s_button5.setFont(new Font("Courier", Font.BOLD, 38));
		s_button5.setTextTransform(VistafarianButton.TEXT_TRANSFORM_ALLCAPS);
		s_button5.setBaseColor(new Color(0xCC99AA));
		s_button5.setShadowDisplayed(true);
		
		s_button6.setFont(new Font("Verdana", 0, 32));
		s_button6.setBaseColor(new Color(0x3399AA));
		s_button6.setShadowDisplayed(true);
		s_button6.addEffect(new ShadowBorder(4, 0, 0, Color.black));
	}

	// Wow, that's a lot of borders
	
	static private Long s_border1 = RoundedBorderMaker.register(24, 4.5f, new Color(0xC5,0xC5,0xC5), new Color(0xF8,0xf8,0xf7));
	static private Long s_border2 = RoundedBorderMaker.register(20, 6, new Color(0xFFDD88), new Color(0xdaeaef));
	static private Long s_shadowborder2 = GenericShadowBorder.register(s_border2, 0, 0, 8f, new Color(0xF8F8F7), Color.black);
	static private Long s_border3 = RoundedBorderMaker.register(20, 5, new Color(0xFFAAAA), Color.white);
	static private Long s_shadowborder3 = GenericShadowBorder.register(s_border3, 3, 3, 5f, new Color(0xdaeaef), GfxEffects.adjustBrightness(new Color(0xdaeaef), 0.7f));
	static private Long s_border4 = GlossyRoundedBorderMaker.register(8, 20, new Color(0xFFDD88), new Color(0xFFFFFF));
	static private Long s_shadowborder4 = GenericShadowBorder.register(s_border4, 0, 0, 6f, new Color(0xdaeaef), GfxEffects.adjustBrightness(new Color(0xdaeaef), 0.2f));
	static private DateFormat s_dateFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US);
	
	static private LayoutInfo s_layout1 = new LayoutInfo(LayoutInfo.UNIT_PIXEL, 100, 100);
	static private LayoutInfo s_layout2 = new LayoutInfo(LayoutInfo.UNIT_PERCENTAGE, 33, 33);
	static {
		ThreeColumnsLayoutResource.register(s_layout1);
		ThreeColumnsLayoutResource.register(s_layout2);
	}

	
    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    public HomePage(final PageParameters parameters) {

        this.innerAdd(ButtonResource.getImage("graphics-disclaimer", s_disclaimerButton, "No Graphic Artists Were Hurt In the Process Of Making This Page!"));
        
        this.innerAdd(ButtonResource.getImage("button1", s_button1, "Swit can"));
        this.innerAdd(ButtonResource.getImage("button2", s_button2, "dynamically generate"));
        this.innerAdd(ButtonResource.getImage("button3", s_button3, "the finest buttons"));
        this.innerAdd(ButtonResource.getImage("button4", s_button4, " (for free) !!"));
        this.innerAdd(ButtonResource.getImage("button5", s_button5, "Ain't it great ?"));

        
        
        Border border1 = new TableImageBorder("border1", s_border1, new Color(0xF8,0xf8,0xf7));
        this.innerAdd(border1);
        Border border2 = new TableImageBorder("border2", s_shadowborder2, new Color(0xdaeaef)); // ImageBorder.createFromBorder("border2", s_margin, 600, 600); 
        border1.add(border2);
        border2.add(new TableImageBorder("border3", s_shadowborder3, Color.white));
        border2.add(new TableImageBorder("border4", s_shadowborder4, Color.white));
         
        ThreeColumnsLayoutBorderFixed layout1;
        this.innerAdd(layout1 = new ThreeColumnsLayoutBorderFixed("layout1", s_layout1));
        ColumnPanel layout1left = layout1.getLeftColumn();
        layout1left.addContent(this.createColumnElement(layout1.getLeftColumn(), "Some text"));
        layout1left.addContent(this.createColumnElement(layout1.getLeftColumn(), "We can stack several elements on each columns"));
        ColumnPanel layout1right = layout1.getRightColumn();
        layout1right.addContent(this.createColumnElement(layout1.getRightColumn(), "Each columns can be dynamically hidden"));
        layout1right.addContent(this.createColumnElement(layout1.getRightColumn(), "when hidden, the central column streches out to fill the empty space"));
        
        LayoutInfo s_layout2temp = new LayoutInfo(LayoutInfo.UNIT_PERCENTAGE, 33, 33);
        ThreeColumnsLayoutResource.register(s_layout2temp);
        
        ThreeColumnsLayoutBorderPc layout2;
        layout1.add(layout2 = new ThreeColumnsLayoutBorderPc("layout2", s_layout2temp));
        ColumnPanel layout2left = layout2.getLeftColumn();
        layout2left.addContent(this.createColumnElement(layout2.getLeftColumn(), "Yes, 33% only here!"));
        layout2left.addContent(this.createColumnElement(layout2.getLeftColumn(), "And still stackable..."));
        
        this.innerAdd(ButtonResource.getTemporaryImage("timebutton", s_button6, s_dateFormatter.format(new Date())));
    }
    
    private Fragment createColumnElement(ColumnPanel col, String text)
    {
    	Fragment frag = new Fragment(col.getContentId(), "column-element", this);
    	frag.add(new Label("element", text));
    	return frag;
    }

	public void renderHead(IHeaderResponse response) 
	{
		super.renderHead(response);
		
		response.renderJavascriptReference(JQuery.getReference());
		response.renderJavascriptReference(JQueryTools.getReference());
		response.renderJavascript(JQuery.getOnReadyScript("$('ul.tabs').tabs('div.panes > div');"), null);
	}
}
