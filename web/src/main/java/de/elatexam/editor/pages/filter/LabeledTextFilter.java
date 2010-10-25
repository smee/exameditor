/*

Copyright (C) 2010 Steffen Dienst

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
package de.elatexam.editor.pages.filter;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.AbstractFilter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

/**
 * Like {@link org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilter<T>} but prepends a label.
 * 
 * @author Steffen Dienst
 * 
 */
public class LabeledTextFilter<T> extends AbstractFilter {
    private static final long serialVersionUID = 1L;

    private final TextField<T> filter;

    /**
     * Constructor
     *
     * @param id
     *            component id
     * @param model
     *            model for the underlying form component
     * @param form
     *            filter form this filter will be added to
     */
    public LabeledTextFilter(String id, IModel<String> labelModel, IModel<T> textFieldModel, FilterForm form) {
        super(id, form);
        add(new Label("lbl", labelModel));
        add(filter = new TextField<T>("tfld", textFieldModel));
        enableFocusTracking(filter);
        add(filter);
    }

    /**
     * @return underlying {@link TextField} form component that represents this filter
     */
    public final TextField<T> getFilter() {
        return filter;
    }

}
