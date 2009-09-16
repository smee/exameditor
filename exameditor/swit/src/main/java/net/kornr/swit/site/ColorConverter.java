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

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class ColorConverter implements Converter {

	public Object convert(Class clzz, Object obj) 
	{
		if (Color.class.equals(clzz) == false)
			throw new ConversionException("Not a color conversion!");
		
		if (Color.class.equals(obj.getClass()))
			return obj;
		
		String value = obj.toString();
		if (value.startsWith("#"))
			value = value.substring(1);
		
		Integer i = Integer.parseInt(value, 16);
		return new Color(i);
	}

}
