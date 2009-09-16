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

/**
 * 
 * A simple object that just stores the padding values to apply on a button.
 *
 */
public class TextPadding 
{
	int m_top, m_bottom, m_left, m_right;

	public TextPadding(int left, int right, int top, int bottom)
	{
		m_left = left;
		m_right = right;
		m_top = top;
		m_bottom = bottom;
	}
	
	public int getTop() {
		return m_top;
	}

	public void setTop(int top) {
		m_top = top;
	}

	public int getBottom() {
		return m_bottom;
	}

	public void setBottom(int bottom) {
		m_bottom = bottom;
	}

	public int getLeft() {
		return m_left;
	}

	public void setLeft(int left) {
		m_left = left;
	}

	public int getRight() {
		return m_right;
	}

	public void setRight(int right) {
		m_right = right;
	}


}
