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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.util.convert.IConverter;

import de.elatexam.model.MappingSubTaskDef.Concept.ConceptCorrectAssignmentIDItem;

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
		final List<ConceptCorrectAssignmentIDItem> result = new ArrayList<ConceptCorrectAssignmentIDItem>();
		for (final String s : StringUtils.split(value, ',')) {
			final ConceptCorrectAssignmentIDItem ca = new ConceptCorrectAssignmentIDItem();
			ca.setItem(s);
			result.add(ca);
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
		final List<ConceptCorrectAssignmentIDItem> list = (List<ConceptCorrectAssignmentIDItem>) value;
		final Iterator stringIterator = IteratorUtils.transformedIterator(list.iterator(), new Transformer() {
			public Object transform(final Object input) {
				return ((ConceptCorrectAssignmentIDItem) input).getItem();
			}
		});
		return StringUtils.join(stringIterator, ",");
	}
}
