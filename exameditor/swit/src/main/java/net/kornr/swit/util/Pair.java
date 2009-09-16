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
package net.kornr.swit.util;

import java.io.Serializable;

public class Pair<A,B> implements Serializable
{
	private A m_first  = null;
	private B m_second = null;
	
	public Pair()
	{
	}
	
	public Pair(A a, B b)
	{
		m_first = a;
		m_second = b;
	}
	
	public void setFirst(A a)
	{
		m_first = a;
	}
	
	public A getFirst()
	{
		return m_first;
	}
	
	public void setSecond(B b)
	{
		m_second = b;
	}
	
	public B getSecond()
	{
		return m_second;
	}
	
	public String toString()
	{
		return "("+m_first+","+m_second+")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((m_first == null) ? 0 : m_first.hashCode());
		result = prime * result
				+ ((m_second == null) ? 0 : m_second.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (m_first == null) {
			if (other.m_first != null)
				return false;
		} else if (!m_first.equals(other.m_first))
			return false;
		if (m_second == null) {
			if (other.m_second != null)
				return false;
		} else if (!m_second.equals(other.m_second))
			return false;
		return true;
	}



}
