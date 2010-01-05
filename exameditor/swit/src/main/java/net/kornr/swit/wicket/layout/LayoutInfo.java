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
package net.kornr.swit.wicket.layout;

import java.awt.Color;
import java.io.Serializable;

import net.kornr.swit.util.StringUtils;

import org.apache.wicket.util.value.ValueMap;

/**
 * Layout info class, used by the ThreeColumnLayoutManager
 * 
 * @author Rodrigo Reyes
 *
 */
public class LayoutInfo implements Serializable 
{
	private String m_name = "layout";
	private String m_internalName = "layout";
	
	static final public int UNIT_EM = 1;
	static final public int UNIT_PIXEL = 2;
	static final public int UNIT_PERCENTAGE = 3;
	
	private int m_leftSize = 0;
	private int m_rightSize = 0;
	private int m_unit = UNIT_EM;
	
	private Color m_leftColor = Color.white;
	private Color m_rightColor = Color.white;
	private Color m_middleColor = Color.white;

	private ValueMap m_map;

	static private long s_counter = 0;
	static final private long s_namecounter = System.currentTimeMillis();
	
	private LayoutInfo(String name)
	{
		m_name = name;
		init();
	}
	
	public LayoutInfo()
	{
		m_name = getUniqueName();
		init();
	}
	
	public LayoutInfo(int unit, int left, int right)
	{
		m_name = getUniqueName();
		m_unit = unit;
		m_leftSize = left;
		m_rightSize = right;
		init();
	}
	
	synchronized private void init()
	{
		// internal name must be a css-compliant name
		m_internalName = m_name;

		m_map = new ValueMap();
		m_map.put("colmask", createUniqueId());
		m_map.put("colright", createUniqueId());
		m_map.put("colmid", createUniqueId());
		m_map.put("colleft", createUniqueId());
		m_map.put("col1", createUniqueId());
		m_map.put("col2", createUniqueId());
		m_map.put("col3", createUniqueId());
		m_map.put("threecol", createUniqueId());
		m_map.put("col1wrap", createUniqueId());
		m_map.put("holygrail", createUniqueId());
	}
	
	public ValueMap getClassId()
	{
		return new ValueMap(m_map);
	}
	
	public int getLeftSize() {
		return m_leftSize;
	}
	public void setLeftSize(int leftSize) {
		m_leftSize = leftSize;
	}
	public int getRightSize() {
		return m_rightSize;
	}
	public void setRightSize(int rightSize) {
		m_rightSize = rightSize;
	}
	public int getUnit() {
		return m_unit;
	}
	public void setUnit(int unit) {
		m_unit = unit;
	}
	public Color getLeftColor() {
		return m_leftColor;
	}
	public void setLeftColor(Color leftColor) {
		m_leftColor = leftColor;
	}
	public Color getRightColor() {
		return m_rightColor;
	}
	public void setRightColor(Color rightColor) {
		m_rightColor = rightColor;
	}
	public Color getMiddleColor() {
		return m_middleColor;
	}
	public void setMiddleColor(Color middleColor) {
		m_middleColor = middleColor;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LayoutInfo)
		{
			LayoutInfo other = (LayoutInfo)obj;
			return (other.m_leftSize==this.m_leftSize) && (other.m_rightSize==this.m_rightSize)
				&& (m_leftColor.equals(other.m_leftColor)) && (m_rightColor.equals(other.m_rightColor))
				&& (m_middleColor.equals(other.m_middleColor))
				&& (m_name.equals(other.m_name))
					;
		}
		return false;
	}

	public String getName() {
		return m_name;
	}

	public void setName(String name) {
		m_name = name;
	}

	@Override
	public int hashCode() 
	{
		return m_name.hashCode() * m_leftSize * m_rightSize + m_unit + (m_leftColor.getRGB()+m_middleColor.getRed()+m_rightColor.getRGB());
	}
	
	static public String toCssValue(Color c)
	{
		return "rgb("+c.getRed()+","+c.getGreen()+","+c.getBlue()+")";
	}
	
	public LayoutInfo duplicate()
	{
		LayoutInfo copy = new LayoutInfo(m_name);
		copy.m_internalName = this.m_internalName;
		copy.m_map = this.m_map;
		copy.m_leftColor = this.m_leftColor;
		copy.m_middleColor = this.m_middleColor;
		copy.m_rightColor = this.m_rightColor;
		copy.m_leftSize = this.m_leftSize;
		copy.m_rightSize = this.m_rightSize;
		copy.m_unit = this.m_unit;
		
		return copy;
	}
	
	public String createUniqueId()
	{
		return "cols"+m_internalName+(StringUtils.toShortestString(useCounter()));
	}

	synchronized private static long useCounter()
	{
		return s_counter++;
	}

	synchronized private static String getUniqueName()
	{
		return StringUtils.toShortestString(s_namecounter) + StringUtils.toShortestString(useCounter());
	}
}
