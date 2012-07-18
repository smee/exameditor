/*

Copyright (C) 2011 Steffen Dienst

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
package de.elatexam.editor.pages.subtaskdefs;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.model.IModel;

import de.elatexam.editor.pages.filter.TagFilter;

/**
 * @author Steffen Dienst
 *
 */
public class TagFilterPropertyColumn<T> extends TextFilteredPropertyColumn<T,String> {

  /**
   * @param displayModel
   * @param sortProperty
   * @param propertyExpression
   */
  public TagFilterPropertyColumn(IModel<String> displayModel, String propertyExpression) {
    super(displayModel, propertyExpression);
    // TODO Auto-generated constructor stub
  }
  /* (non-Javadoc)
   * @see org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn#getFilter(java.lang.String, org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm)
   */
  @Override
  public Component getFilter(String componentId, FilterForm<?> form) {
    return new TagFilter(componentId, form);
  }

}
