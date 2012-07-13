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
package de.elatexam.editor.components.panels.tasks.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.util.convert.IConverter;


/**
 * @author Steffen Dienst
 * 
 */
public class CorrectAssignementConverter implements IConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String,
	 * java.util.Locale)
	 */
	public Object convertToObject(final String value, final Locale locale) {
		final List<String> result = new ArrayList<String>();
		for (final String s : StringUtils.split(value, ',')) {
			result.add(s);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object,
	 * java.util.Locale)
	 */
	public String convertToString(final Object value, final Locale locale) {
		final List<String> list = (List<String>) value;
		return StringUtils.join(list.iterator(), ",");
	}
}
