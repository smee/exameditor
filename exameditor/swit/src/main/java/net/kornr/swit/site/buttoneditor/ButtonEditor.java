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
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import net.kornr.swit.button.AmazonianButton;
import net.kornr.swit.button.ButtonResource;
import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.button.GlassyButton;
import net.kornr.swit.button.SoftShiningButton;
import net.kornr.swit.button.VistafarianButton;
import net.kornr.swit.button.WebTwoButton;
import net.kornr.swit.button.effect.Effect;
import net.kornr.swit.button.effect.ShadowBorder;
import net.kornr.swit.site.BasePage;
import net.kornr.swit.site.jquery.JQuery;
import net.kornr.swit.site.jquery.colorpicker.ColorPickerField;
import net.kornr.swit.site.util.MutableResourceReferenceLink;
import net.kornr.swit.site.widget.EffectChoicePanel;
import net.kornr.swit.util.Pair;
import net.kornr.swit.wicket.border.TableImageBorder;
import net.kornr.swit.wicket.border.graphics.GenericShadowBorder;
import net.kornr.swit.wicket.border.graphics.RoundedBorderMaker;
import net.kornr.swit.wicket.border.graphics.SizedBorder;
import net.kornr.swit.wicket.layout.ColumnPanel;
import net.kornr.swit.wicket.layout.LayoutInfo;
import net.kornr.swit.wicket.layout.ThreeColumnsLayoutManager;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutResource;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ImageButton;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.apache.wicket.util.value.ValueMap;


public class ButtonEditor extends BasePage implements IHeaderContributor
{
	static private Color s_blocColor = Color.white;

	static private Long s_border = RoundedBorderMaker.register(12, 3f, new Color(0xC5,0xC5,0xFF), s_blocColor); //  new Color(0xF8,0xf8,0xf7));
	static private Long s_shadow = GenericShadowBorder.register(s_border, 0, 0, 4f, null, Color.black);

	static private GradientPaint s_grad = new GradientPaint(new Point2D.Float(10,10), new Color(0xdaeaef), new Point2D.Float(10,80), Color.white);
	static private Long s_border2 = RoundedBorderMaker.register(5, 2, new Color(0xC5,0xC5,0xC5), s_grad);
	static private Long s_big = SizedBorder.register(s_border2, 1200, 800);

	static private Long s_border3 = RoundedBorderMaker.register(5, 2, new Color(0xC5,0xC5,0xC5), null);

	static private Font s_defaultButtonFont = new Font("Verdana", Font.BOLD, 24);

	static private net.kornr.swit.button.WebTwoButton s_buttonTemplate = new net.kornr.swit.button.WebTwoButton(new Color(0x7799DD));
	static {
		s_buttonTemplate.setFont(new Font("Arial", Font.BOLD, 18));
		s_buttonTemplate.setWidth(250);
		s_buttonTemplate.setHeight(24);
		s_buttonTemplate.setFontColor(new Color(0x000000));
		s_buttonTemplate.setAutoExtend(Boolean.TRUE);
		s_buttonTemplate.setShadowDisplayed(Boolean.FALSE);
		s_buttonTemplate.addEffect(new ShadowBorder(3,0,0,Color.black));
		s_buttonTemplate.setBaseColor(Color.white);
		s_buttonTemplate.setLineWidth(1);
	}

	static private ButtonTemplate s_logoTemplate = new VistafarianButton(new Color(0x7799DD));
	static {
		s_logoTemplate.setFont(new Font("Verdana", Font.BOLD, 24));
		s_logoTemplate.setWidth(600);
		s_logoTemplate.setHeight(24);
		s_logoTemplate.setFontColor(new Color(0xFFFFFF));
		s_logoTemplate.setAutoExtend(Boolean.TRUE);
		s_logoTemplate.setShadowDisplayed(Boolean.TRUE);
		s_logoTemplate.addEffect(new ShadowBorder(6,0,0, new Color(0x222222)));
	}

	private LinkedList<Pair<String,WebMarkupContainer>> m_propertiesContainer = new  LinkedList<Pair<String,WebMarkupContainer>>();

	private List<ButtonDescriptor> s_buttons = Arrays.asList(new ButtonDescriptor[] {

			new ButtonDescriptor("Vistafarian 1", "Vistafarian with a single base color", VistafarianButton.class.getCanonicalName(), new ButtonProperty[] {
				new ButtonProperty("Base Color", "baseColor", "#999999", ButtonProperty.Type.TYPE_COLOR),
				new ButtonProperty("Round Size", "roundSize", new Float(5f), ButtonProperty.Type.TYPE_FLOAT)
			}),

			new ButtonDescriptor("Vistafarian 2", "Vistafarian with a two distinct colors", VistafarianButton.class.getCanonicalName(), new ButtonProperty[] {
				new ButtonProperty("Top Color", "topColor", "#9999DD", ButtonProperty.Type.TYPE_COLOR),
				new ButtonProperty("Bottom Color", "bottomColor", "#7744FF", ButtonProperty.Type.TYPE_COLOR),
				new ButtonProperty("Round Size", "roundSize", new Float(5f), ButtonProperty.Type.TYPE_FLOAT)
			}),

			new ButtonDescriptor("Amazonian", "Amazonian button", AmazonianButton.class.getCanonicalName(), new ButtonProperty[] {
				new ButtonProperty("Inner Color", "innerColor", "#fce000", ButtonProperty.Type.TYPE_COLOR),
				new ButtonProperty("Outer Color", "outerColor", "#fc8900", ButtonProperty.Type.TYPE_COLOR),
				new ButtonProperty("Invert Icon Position", "rightHanded", Boolean.FALSE, ButtonProperty.Type.TYPE_BOOLEAN)
			}),	

			new ButtonDescriptor("WebTwo", "Web Two button", WebTwoButton.class.getCanonicalName(), new ButtonProperty[] {
				new ButtonProperty("Base Color", "baseColor", "#9855AA", ButtonProperty.Type.TYPE_COLOR)
			}),	

			new ButtonDescriptor("Soft Shining", "Soft Shining Button", SoftShiningButton.class.getCanonicalName(), new ButtonProperty[] {
				new ButtonProperty("Base Color", "baseColor", "#DFDF77", ButtonProperty.Type.TYPE_COLOR),
				new ButtonProperty("Round Size", "roundSize", new Float(30f), ButtonProperty.Type.TYPE_FLOAT),
				new ButtonProperty("Border Size", "lineWidth", new Float(1.5f), ButtonProperty.Type.TYPE_FLOAT),
				new ButtonProperty("Border Color", "lineColor", "#DFDF77", ButtonProperty.Type.TYPE_COLOR)
			}),
			
			new ButtonDescriptor("GLASSY!", "Glassy Button", GlassyButton.class.getCanonicalName(), new ButtonProperty[] {
				new ButtonProperty("Base Color", "baseColor", "#666666", ButtonProperty.Type.TYPE_COLOR),
				new ButtonProperty("Round Size", "roundSize", new Float(30f), ButtonProperty.Type.TYPE_FLOAT),
				new ButtonProperty("Border Size", "lineWidth", new Float(1.5f), ButtonProperty.Type.TYPE_FLOAT),
				new ButtonProperty("Border Color", "lineColor", "#555555", ButtonProperty.Type.TYPE_COLOR)
			})


	});

	static private HashMap<String, ButtonTemplate> s_buttonsTemplates = new HashMap<String,ButtonTemplate>();

	static private LayoutInfo s_layout = new LayoutInfo(LayoutInfo.UNIT_PERCENTAGE, 50, 50);
	static {
		ThreeColumnsLayoutResource.register(s_layout);
		s_layout.setRightColor(BasePage.getInnerColor());
		s_layout.setLeftColor(BasePage.getInnerColor());
		s_layout.setMiddleColor(BasePage.getInnerColor());
	}

	final private ButtonProperty PROPERTY_FONT = new ButtonProperty("Font", "font", new Font("Arial", Font.BOLD, 18), ButtonProperty.Type.TYPE_FONT); 
	final private ButtonProperty PROPERTY_WIDTH = new ButtonProperty("Width", "width", new Integer(300), ButtonProperty.Type.TYPE_INTEGER); 
	final private ButtonProperty PROPERTY_HEIGHT = new ButtonProperty("Height", "height", new Integer(24), ButtonProperty.Type.TYPE_INTEGER);
	final private ButtonProperty PROPERTY_AUTO_EXTEND = new ButtonProperty("Auto-extend the button size", "autoExtend", Boolean.TRUE, ButtonProperty.Type.TYPE_BOOLEAN);
	final private ButtonProperty PROPERTY_FONT_COLOR = new ButtonProperty("Text Color", "fontColor", "#FFFFFF", ButtonProperty.Type.TYPE_COLOR);
	final private ButtonProperty PROPERTY_FONT_SHADOW = new ButtonProperty("Add a drop shadow to the text", "shadowDisplayed", Boolean.TRUE, ButtonProperty.Type.TYPE_BOOLEAN);

	final private static Pattern s_filenamePattern = Pattern.compile("[^a-zA-Z0-9]");

	private ButtonDescriptor m_selectedDescriptor = s_buttons.get(0);
	private String m_text = "Your Text Here";
	private Image m_sample;
	private FeedbackPanel m_feedback;
	private WebMarkupContainer m_properties;
	private WebMarkupContainer m_propEditors;
	private int m_shadowEffect = 1;
	private int m_mirrorEffect = 0;
	private ButtonCodeMaker m_codeEncoder;
	private MutableResourceReferenceLink m_downloadLink;

	private List<ButtonProperty> m_currentProperties = new LinkedList<ButtonProperty>();

	private String bgcolor = "FFFFFF";

	public List<ButtonDescriptor> getAvailableButtons()
	{
		return s_buttons;
	}

	public ButtonEditor(PageParameters params)
	{
		init();
	}

	private void init()
	{
		this.innerAdd(new Image("logo", ButtonResource.getReference(), ButtonResource.getValueMap(s_logoTemplate, "The Swit Buttons Generator")));

		m_codeEncoder = new ButtonCodeMaker(m_selectedDescriptor, m_currentProperties, new PropertyModel<String>(this, "text"));

		final Form form = new Form("form") {

			@Override
			protected void onSubmit() {
				if (((WebRequest)(WebRequestCycle.get().getRequest())).isAjax() == false)
					createButton(null);
			}
		};
		this.innerAdd(form);

		Border sampleborder = new TableImageBorder("sampleborder", s_border3, Color.white);
		form.add(sampleborder);
		WebMarkupContainer samplecont = new WebMarkupContainer("samplecontainer");
		sampleborder.add(samplecont);
		samplecont.add((m_sample = new Image("sample")).setOutputMarkupId(true));
		sampleborder.add(new ColorPickerField("samplebgcolor", new PropertyModel<String>(this, "bgcolor"), samplecont));
		ImageButton submit = new ImageButton("submit", ButtonResource.getReference(), ButtonResource.getValueMap(s_buttonTemplate, "Update that button, now!"));
		sampleborder.add(submit);
		submit.add(new AjaxFormSubmitBehavior(form, "onclick") {
			@Override
			protected void onError(AjaxRequestTarget arg0) {
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				createButton(target);
			}

			@Override
			protected CharSequence getEventHandler()
			{
				return new AppendingStringBuffer(super.getEventHandler()).append("; return false;");
			}
		});
		sampleborder.add(m_downloadLink = new MutableResourceReferenceLink("downloadbutton", ButtonResource.getReference(), null));
		m_downloadLink.setOutputMarkupId(true);

		//		this.innerAdd(m_codeLabel = new Label("code", new PropertyModel(m_codeEncoder, "code")));
		//		m_codeLabel.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true).setEscapeModelStrings(false);
		//		m_codeLabel.setVisible(true);
		final ModalWindow codewindow = new ModalWindow("code");
		this.innerAdd(codewindow);
		Fragment codefrag = new Fragment(codewindow.getContentId(), "codepanel", this);
		Label lcode = new Label("code", new PropertyModel(m_codeEncoder, "code"));
		codefrag.add(lcode);
		codewindow.setContent(codefrag);
		codewindow.setTitle("Java Code");
		codewindow.setCookieName("switjavacodewindow");

		sampleborder.add(new AjaxLink("showwindowcode") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				codewindow.show(target);
			}
		});

		form.add((m_feedback = new FeedbackPanel("feedback")).setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true));

		ThreeColumnsLayoutManager layout = new ThreeColumnsLayoutManager("2col-layout", s_layout);
		form.add(layout);		
		ColumnPanel rightcol = layout.getRightColumn();
		ColumnPanel leftcol = layout.getLeftColumn();

		Border textborder = new TableImageBorder("textborder", s_shadow, s_blocColor);
		layout.add(textborder);
		textborder.add(new TextField<String>("button-text", new PropertyModel<String>(this, "text")));

		Border buttonsborder = new TableImageBorder("buttonsborder", s_shadow, s_blocColor);
		layout.add(buttonsborder);
		buttonsborder.add(new ListView<ButtonDescriptor>("types", s_buttons) {
			@Override
			protected void populateItem(ListItem<ButtonDescriptor> item) 
			{
				final IModel<ButtonDescriptor> model = item.getModel(); 
				ButtonDescriptor bd = item.getModelObject();

				ButtonTemplate tmpl = s_buttonsTemplates.get(bd.getName());
				if (tmpl == null)
				{
					tmpl = bd.createTemplate();
					try {
						List<ButtonProperty> props = bd.getProperties(); 
						bd.applyProperties(tmpl, props);
						tmpl.setWidth(200);
						tmpl.setFont(s_defaultButtonFont);
						tmpl.setFontColor(Color.white);
						tmpl.setShadowDisplayed(true);
						tmpl.addEffect(new ShadowBorder(4, 0, 0, Color.black));
						tmpl.setAutoExtend(true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					s_buttonsTemplates.put(bd.getName(), tmpl);
				}

				ImageButton button = new ImageButton("sample", ButtonResource.getReference(), ButtonResource.getValueMap(tmpl, bd.getName()));
				item.add(button);
				button.add(new AjaxFormSubmitBehavior(form, "onclick") {
					@Override
					protected void onError(AjaxRequestTarget arg0) {
					}

					@Override
					protected void onSubmit(AjaxRequestTarget target) {
						m_selectedDescriptor = model.getObject();
						m_currentProperties = m_selectedDescriptor.getProperties();
						if (target != null)
						{
							// target.addComponent(m_properties);
						}
						createButton(target);
					}

					@Override
					protected CharSequence getEventHandler()
					{
						String hider = getJQueryCodeForPropertiesHiding(model.getObject());
						return new AppendingStringBuffer(hider+";"+super.getEventHandler()).append("; return false;");
					}
				});

			}
		});


		m_properties = new TableImageBorder("propertiesborder", s_shadow, s_blocColor);
		layout.add(m_properties);
		m_properties.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
		m_currentProperties = m_selectedDescriptor.getProperties();

		m_propEditors = new ListView<ButtonDescriptor>("property", s_buttons) {
			@Override
			protected void populateItem(ListItem<ButtonDescriptor> item) {
				ButtonDescriptor desc = item.getModelObject();
				WebMarkupContainer container = new WebMarkupContainer("container");
				item.add(container);
				PropertyListEditor lst = new PropertyListEditor("lst", desc.getProperties());
				container.add(lst);
				container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
				m_propertiesContainer.add(new Pair(desc.getName(), container));
			}
		};

		m_properties.add(m_propEditors);

		//		Border fontborder = new TableImageBorder("fontborder", s_shadow, s_blocColor);
		//		form.add(fontborder);
		//		fontborder.add(new ButtonPropertyEditorPanel("fontselector", PROPERTY_FONT, false));
		//		fontborder.add(new ButtonPropertyEditorPanel("fontcolor", PROPERTY_FONT_COLOR, false));
		//		fontborder.add(new ButtonPropertyEditorPanel("fontshadow", PROPERTY_FONT_SHADOW, true));

		rightcol.addContent(createFragment(ColumnPanel.CONTENT_ID, Arrays.asList(new Component[] {
				new ButtonPropertyEditorPanel("element", PROPERTY_WIDTH, true),
				new ButtonPropertyEditorPanel("element", PROPERTY_HEIGHT, true),
				new ButtonPropertyEditorPanel("element", PROPERTY_AUTO_EXTEND, true)
		}), "Button Size"));

		rightcol.addContent(createFragment(rightcol.CONTENT_ID, Arrays.asList(new Component[] {
				new ButtonPropertyEditorPanel("element", PROPERTY_FONT, false),
				new ButtonPropertyEditorPanel("element", PROPERTY_FONT_COLOR, true),
				new ButtonPropertyEditorPanel("element", PROPERTY_FONT_SHADOW, true)
		}), "Font Selection"));

		rightcol.addContent(createFragment(rightcol.CONTENT_ID,	new EffectChoicePanel("element", new PropertyModel<Integer>(this, "shadowEffect"), EffectUtils.getShadowEffects()),
		"Shadow Effect"));
		rightcol.addContent(createFragment(rightcol.CONTENT_ID,	new EffectChoicePanel("element", new PropertyModel<Integer>(this, "mirrorEffect"), EffectUtils.getMirrorEffects()),
		"Mirror Effect"));

		createButton(null);
	}

	public ButtonDescriptor getSelectedDescriptor() {
		return m_selectedDescriptor;
	}

	public void setSelectedDescriptor(ButtonDescriptor selectedDescriptor) {
		m_selectedDescriptor = selectedDescriptor;
	}

	public String getText() {
		return m_text;
	}

	public void setText(String text) {
		m_text = text;
	}

	private void createButton(AjaxRequestTarget target)
	{
		ButtonDescriptor desc = m_selectedDescriptor;

		if (desc == null)
		{
			error("Please select a template");
		}
		else
		{
			ButtonTemplate bt = desc.createTemplate();
			try {
				desc.applyProperties(bt, m_currentProperties);
				LinkedList<ButtonProperty> props = new LinkedList<ButtonProperty>();
				props.add(PROPERTY_FONT);
				props.add(PROPERTY_WIDTH);
				props.add(PROPERTY_HEIGHT);
				props.add(PROPERTY_FONT_COLOR);
				props.add(PROPERTY_AUTO_EXTEND);
				props.add(PROPERTY_FONT_SHADOW);

				desc.applyProperties(bt, props);

				Effect e = EffectUtils.getShadowEffect(m_shadowEffect);
				if (e != null)
					bt.addEffect(e);

				e= EffectUtils.getMirrorEffect(m_mirrorEffect);
				if (e != null)
					bt.addEffect(e);

				LinkedList<String> effects = new LinkedList<String>();
				String shadow = EffectUtils.getShadowJavaCode(m_shadowEffect);
				String mirror = EffectUtils.getMirrorJavaCode(m_mirrorEffect);
				if (shadow != null)
					effects.add(shadow);
				if (mirror != null)
					effects.add(mirror);

				m_codeEncoder.setClassProperties(m_currentProperties);
				m_codeEncoder.setDescriptor(desc);
				m_codeEncoder.setProperties(props);
				m_codeEncoder.setEffects(effects);

			} catch (Exception exc)
			{
				exc.printStackTrace();
			}

			String text = m_text!=null?m_text:"(empty)";
			String filename = s_filenamePattern.matcher(text).replaceAll("");
			if (filename.length()==0)
				filename="image";

			m_sample.setImageResourceReference(ButtonResource.getReference(), ButtonResource.getTemporaryValueMap(bt, text, false));
			m_downloadLink.setResourceParameters(ButtonResource.getTemporaryValueMap(bt, text, true, filename));
		}

		if (target != null)
		{
			target.addComponent(m_downloadLink);
			//			target.addComponent(m_codeLabel);
			target.addComponent(m_sample);
			target.addComponent(m_feedback);
		}
	}

	public List<ButtonProperty> getCurrentProperties() {
		return m_currentProperties;
	}

	public void setCurrentProperties(List<ButtonProperty> currentProperties) {
		m_currentProperties = currentProperties;
	}

	public int getShadowEffect() {
		return m_shadowEffect;
	}

	public void setShadowEffect(int shadowEffect) {
		m_shadowEffect = shadowEffect;
	}

	public int getMirrorEffect() {
		return m_mirrorEffect;
	}

	public void setMirrorEffect(int mirrorEffect) {
		m_mirrorEffect = mirrorEffect;
	}

	public void renderHead(IHeaderResponse response) 
	{
		super.renderHead(response);

		response.renderJavascriptReference(JQuery.getReference());
		response.renderJavascript(JQuery.getOnReadyScript(getJQueryCodeForPropertiesHiding(m_selectedDescriptor)), null);
	}

	public String getJQueryCodeForPropertiesHiding(ButtonDescriptor desc)
	{
		StringBuffer buffer = new StringBuffer();
		for (Pair<String, WebMarkupContainer>p : m_propertiesContainer)
		{
			if (p.getFirst().equals(desc.getName()))
				buffer.append("$('#" + p.getSecond().getMarkupId()+"').show();");
			else
				buffer.append("$('#" + p.getSecond().getMarkupId()+"').hide();");
		}
		return buffer.toString();
	}


	private Fragment createFragment(String id, Component comp, String title)
	{
		ArrayList<Component> arr = new ArrayList<Component>(1);
		arr.add(comp);
		return createFragment(id, arr, title);
	}

	private Fragment createFragment(String id, List<Component> comps, String title)
	{
		Fragment frag = new Fragment(id, "logicalelementlist", this);
		Border border = new TableImageBorder("border", s_shadow, s_blocColor);
		frag.add(border);
		if (title == null)
			border.add(new Label("title", "").setVisible(false));
		else
			border.add(new Label("title", title));
		border.add(new ListView<Component>("list", comps) {
			@Override
			protected void populateItem(ListItem<Component> item) 
			{
				item.add(item.getModelObject());
			}

		});
		return frag;
	}


}
