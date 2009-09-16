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

public class MappedString 
{
	final static private int NORMAL 	= 1;
	final static private int DOLLAR 	= 2;
	final static private int OPEN 	= 3;
	final static private int CLOSE 	= 4;
	
	private String m_str;
	
	public MappedString(String str)
	{
		m_str = str;
	}
	
	public String map(String... strings)
	{
		int maxitem = strings.length;
		int len = m_str.length();
		StringBuffer result = new StringBuffer(len+64);
		StringBuffer varbuf = new StringBuffer(2);
		
		int state = NORMAL;
		for (int i=0; i<len; i++)
		{
			char c = m_str.charAt(i);
			switch(state)
			{
			case MappedString.NORMAL:
				if (c=='$')
					state = DOLLAR;
				else
					result.append(c);
				break;
	
			case MappedString.DOLLAR:
				if (c=='{')
					state = OPEN;
				else
				{
					result.append('$');
					result.append(c);
					state = NORMAL;
				}
				break;
				
			case MappedString.OPEN:
			
				int count = Character.getNumericValue(c);
				if (count >= 0 && count < 10)
				{
					// added: 2 lines below
					varbuf.setLength(0);
					varbuf.append(c);
//					result.append(strings[count]);
					state = CLOSE;
				}
				else
				{
					result.append("${");
					result.append(c);
					state = NORMAL;
				}
				break;
				
			case MappedString.CLOSE:
				if (c=='}')
				{
					int indx = Integer.parseInt(varbuf.toString());
					if (indx >= maxitem)
						throw new RuntimeException("Out of array index, found index " + indx + ", but the max is " + maxitem);
					result.append(strings[indx]);
					state = NORMAL;
				}
				else
				{
					int ccount = Character.getNumericValue(c);
					if (ccount >= 0 && ccount < 10)
					{
						varbuf.append(c);
					}					
					else
					{
						result.append("${");
						result.append(varbuf.toString());
						result.append(c);
						state = NORMAL;						
					}
				}
				break;
			}
		}
		
		return result.toString();
	}
	
}
