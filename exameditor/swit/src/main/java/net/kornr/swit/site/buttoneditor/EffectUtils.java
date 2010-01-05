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
import java.util.LinkedList;
import java.util.List;

import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.button.WebTwoButton;
import net.kornr.swit.button.effect.Effect;
import net.kornr.swit.button.effect.ShadowBorder;
import net.kornr.swit.button.effect.VerticalMirror;
import net.kornr.swit.util.Pair;

import org.apache.wicket.model.IModel;


public class EffectUtils 
{
	static private Font s_smallFont = new Font("Verdana", Font.BOLD, 12);

	static private List<Pair<String,IModel<ButtonTemplate>>> s_mirrors = null;
	static private List<String> s_mirrorsJavaCode = null;
	
	static private List<Pair<String,IModel<ButtonTemplate>>> s_shadows = null;
	static private List<String> s_shadowJavaCode = null;
	
	public static List<Pair<String,IModel<ButtonTemplate>>> getMirrorEffects()
	{
		if (s_mirrors == null)
		{
			// temp variable, to avoid racing issues
			LinkedList<Pair<String,IModel<ButtonTemplate>>> mirrors = new LinkedList<Pair<String,IModel<ButtonTemplate>>>();
			mirrors.add(new Pair<String,IModel<ButtonTemplate>>("No mirror", createMirrorForDisplay(null)));
			mirrors.add(new Pair<String,IModel<ButtonTemplate>>("Mirror 1", createMirrorForDisplay(new VerticalMirror(0.50f, 0.25f))));
			mirrors.add(new Pair<String,IModel<ButtonTemplate>>("Mirror 2", createMirrorForDisplay(new VerticalMirror(0.50f, 0.50f))));
			mirrors.add(new Pair<String,IModel<ButtonTemplate>>("Mirror 3", createMirrorForDisplay(new VerticalMirror(0.50f, 0.75f))));
			mirrors.add(new Pair<String,IModel<ButtonTemplate>>("Mirror 4", createMirrorForDisplay(new VerticalMirror(0.75f, 0.50f))));
			mirrors.add(new Pair<String,IModel<ButtonTemplate>>("Mirror 5", createMirrorForDisplay(new VerticalMirror(0.75f, 0.75f))));
	
			LinkedList<String> java = new LinkedList<String>();
			java.add(null);
			java.add("new VerticalMirror(0.50f, 0.25f)");
			java.add("new VerticalMirror(0.50f, 0.50f)");
			java.add("new VerticalMirror(0.50f, 0.75f)");
			java.add("new VerticalMirror(0.75f, 0.50f)");
			java.add("new VerticalMirror(0.75f, 0.75f)");
			
			s_mirrors = mirrors;
			s_mirrorsJavaCode = java;
		}
		
		return s_mirrors;
	}
	
	public static String getMirrorJavaCode(int index)
	{
		if (index >= 1 && index < s_mirrors.size())
			return s_mirrorsJavaCode.get(index);
		
		return null;		
	}
	
	public static Effect getMirrorEffect(int index)
	{
		if (index >= 1 && index < s_mirrors.size())
			return s_mirrors.get(index).getSecond().getObject().getEffects().get(0);
		
		return null;
	}

	static IModel<ButtonTemplate> createMirrorForDisplay(Effect e)
	{
		ButtonTemplate tmpl = new WebTwoButton(new Color(0x6666CC));
		if (e != null)
			tmpl.addEffect(e);
		tmpl.setFont(s_smallFont);
		tmpl.setFontColor(Color.black);
		tmpl.setHeight(24);
		
		Long id = StaticButtonTemplateModel.register(tmpl);
		
		return new StaticButtonTemplateModel(id);
	}

	public static List<Pair<String,IModel<ButtonTemplate>>> getShadowEffects()
	{
		if (s_shadows == null)
		{
			// temp variable, to avoid racing issues
			LinkedList<Pair<String,IModel<ButtonTemplate>>> shadows = new LinkedList<Pair<String,IModel<ButtonTemplate>>>();
			shadows.add(new Pair<String,IModel<ButtonTemplate>>("No mirror", createShadowForDisplay(null)));
			shadows.add(new Pair<String,IModel<ButtonTemplate>>("Glow 1", createShadowForDisplay(new ShadowBorder(4,0,0,Color.black))));
			shadows.add(new Pair<String,IModel<ButtonTemplate>>("Glow 2", createShadowForDisplay(new ShadowBorder(8,0,0,Color.black))));
			shadows.add(new Pair<String,IModel<ButtonTemplate>>("Shadow 1", createShadowForDisplay(new ShadowBorder(3,1,1,Color.gray))));
			shadows.add(new Pair<String,IModel<ButtonTemplate>>("Shadow 2", createShadowForDisplay(new ShadowBorder(4,2,2,Color.gray))));
			shadows.add(new Pair<String,IModel<ButtonTemplate>>("Shadow 3", createShadowForDisplay(new ShadowBorder(6,4,4,Color.gray))));

			LinkedList<String> java = new LinkedList<String>();
			java.add(null);
			java.add("new ShadowBorder(8,0,0,Color.black)");
			java.add("new ShadowBorder(16,0,0,Color.black)");
			java.add("new ShadowBorder(3,1,1,Color.gray)");
			java.add("new ShadowBorder(4,2,2,Color.gray)");
			java.add("new ShadowBorder(8,4,4,Color.gray)");

			s_shadows = shadows;
			s_shadowJavaCode = java;
		}
		
		return s_shadows;
	}

	public static Effect getShadowEffect(int index)
	{
		if (index >= 1 && index < s_shadows.size())
			return s_shadows.get(index).getSecond().getObject().getEffects().get(0);
		
		return null;
	}

	static IModel<ButtonTemplate> createShadowForDisplay(Effect e)
	{
		ButtonTemplate tmpl = new WebTwoButton(Color.white);
		if (e != null)
			tmpl.addEffect(e);
		tmpl.setFont(s_smallFont);
		tmpl.setFontColor(Color.black);
		tmpl.setHeight(24);
		
		Long id = StaticButtonTemplateModel.register(tmpl);
		
		return new StaticButtonTemplateModel(id);
	}

	public static String getShadowJavaCode(int index)
	{
		if (index >= 1 && index < s_mirrors.size())
			return s_shadowJavaCode.get(index);
		
		return null;
		
	}

}
