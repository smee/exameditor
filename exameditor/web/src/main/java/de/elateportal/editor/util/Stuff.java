/*

Copyright (C) 2009 Steffen Dienst

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package de.elateportal.editor.util;

import java.lang.reflect.Method;
import java.util.Map;

import de.elateportal.model.AddonSubTaskDef;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.SubTaskDefType;
import de.elateportal.model.TextSubTaskDef;

/**
 * Misc. helper functions.
 * 
 * @author Steffen Dienst
 * 
 */
public class Stuff {
	private static Map<Class<?>, String> refNames = new java.util.HashMap<Class<?>, String>() {
		{
			put(McSubTaskDef.class, "Mc");
			put(AddonSubTaskDef.class, "Addon");
			put(ClozeSubTaskDef.class, "Cloze");
			put(TextSubTaskDef.class, "Text");
			put(MappingSubTaskDef.class, "Mapping");
			put(PaintSubTaskDef.class, "Paint");
		}
	};

	/**
	 * Call a no-parameter method, return it's value. Rethrow whatever
	 * {@link Class#getMethod(String, Class...)} or
	 * {@link Method#invoke(Object, Object...)} throws.
	 * 
	 * @param o
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public static Object call(Object o, String methodName) throws Exception {
		Method method = o.getClass().getMethod(methodName);
		return method.invoke(o);
	}

	/**
	 * Invoke similary named methods involving {@link SubTaskDefType}s.
	 * 
	 * @param o
	 *          the object the method should be invoked on
	 * @param methodNameTemplate
	 *          string including %s for the subtaskdef specific name
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static Object call(Object o, String methodNameTemplate, Class<? extends SubTaskDefType> clazz) throws Exception {
		Method method = o.getClass().getMethod(String.format(methodNameTemplate, refNames.get(clazz)));
		return method.invoke(o);
	}

}
