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

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import net.kornr.swit.site.Download;

public class StringUtils 
{
	static public String toHexColor(Color col)
	{
		StringBuffer res = new StringBuffer();
		res.append("#");
		String val = Long.toHexString((long)col.getRGB()&0xFFFFFF);
		for (int i=0; i<(6-val.length());i++)
			res.append("0");
		res.append(val);
		return res.toString();
	}
	
	static public String getSuffix(String name)
	{
		int suffixindx = name.lastIndexOf('.');
		if (suffixindx >= 0)
		{
			return name.substring(suffixindx+1);
		}
		else
			return null;
	}
	
	static public String join(List<? extends Object> list)
	{
		return join(list, ", ");
	}
	
	static public String join(List<? extends Object> list, String separator)
	{
		StringBuffer res = new StringBuffer();
		boolean first = true;
		for (Object o: list)
		{
			if (first == false)
				res.append(separator);
			else
				first = false;
			res.append(o.toString());
		}
		return res.toString();
	}
	
	static public boolean isEmpty(String txt)
	{
		if (txt == null)
			return true;
		
		if (txt.trim().length()==0)
			return true;
		return false;
	}

	static public String capitalize(String str)
	{
		if (str.length()==0)
			return "";
		
		StringBuffer res = new StringBuffer(str.length());
		res.append(Character.toTitleCase(str.charAt(0)));
		if (str.length()>1)
			res.append(str.substring(1));
		return res.toString();
	}
	
	static public String unNullify(String s)
	{
		if (s == null)
			return "";
		return s;
	}

	static public String toString(Exception exc)
	{
		if (exc == null)
			return "";
		
		ByteArrayOutputStream error = new ByteArrayOutputStream();
		exc.printStackTrace(new PrintStream(error));
		return error.toString();
	}

	static char s_azalphabet[] = new char[] { 
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q','r','s','t','u','v','w','x','y','z',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
		
	};

	static char s_humanrewriteablealphabet[] = new char[] { 
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q','r','s','t','u','v','w','x','y','z',
		'1', '2', '3', '4', '5', '6', '7', '8', '9'
		
	};


	static public String toShortestString(long l)
	{
		return toShortestString(l, s_azalphabet);
	}

	static public String toShortestRewritableString(long l)
	{
		return toShortestString(l, s_humanrewriteablealphabet);
	}
	
	static private  String toShortestString(long l, char[] alphabet)
	{
		int alphabetCount = alphabet.length;
		StringBuffer res = new StringBuffer();
		while (l>0)
		{
			long r = (l%alphabetCount);
			l = l/alphabetCount;
			res.insert(0, alphabet[(int)r]);
		}
		
		return res.toString();
	}
	
	static public double getDoubleValue(String str)
	{
		str = str.replace(" ", "");
		str = str.replace("'", "");
		str = str.replace(",", ".");
		return Double.parseDouble(str);
	}

	static public String toString(Object o)
	{
		if (o == null)
			return "[null]";
		
		return o.toString();
	}

	static public String truncate(String str, int length)
	{
		if (length > str.length())
			return str;
		
		char c;
		int index = length;
		while ((Character.isWhitespace(c=str.charAt(index))==false) && (index>0))
		{
			index--;
		}
		if (index==0)
			return str.substring(0, length)+"...";
		
		return str.substring(0, index)+"...";
	}

	static public String load(Class clzz, String resource, String def)
	{
		String result = null;
		InputStream srcin = Download.class.getResourceAsStream(resource);
		if (srcin != null)
		{
			result = StringUtils.load(srcin);
		}
		else
		{
			result = def;
		}
		return result;
	}
	
	static public String load(InputStream in)
	{
		if (in == null)
			return "";

		StringBuffer buffer = new StringBuffer();
		try {
			int b;
			while ( (b=in.read()) != -1)
			{
				switch (b)
				{
				case '<':
					buffer.append("&lt;");
					break;
				case '>':
					buffer.append("&gt;");
					break;
				default:
					buffer.append((char)b);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buffer.toString();
	}

}
