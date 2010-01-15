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
package de.elateportal.editor.components.panels;

import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.model.IModel;

/**
 * Listview that adds css class "odd" and "even" to each item according to it's
 * index.
 * 
 * @author Steffen Dienst
 * 
 */
public abstract class AlternatingListView<T> extends ListView<T> {

	/**
	 * @param id
	 */
	public AlternatingListView(String id) {
		super(id);
	}

	/**
	 * @param id
	 * @param model
	 */
	public AlternatingListView(String id, IModel<? extends List<? extends T>> model) {
		super(id, model);
	}

	/**
	 * @param id
	 * @param list
	 */
	public AlternatingListView(String id, List<? extends T> list) {
		super(id, list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.markup.html.list.ListView#newItem(int)
	 */
	@Override
	protected ListItem<T> newItem(final int index) {
		return new OddEvenListItem<T>(index, getListItemModel(getModel(), index));
	}

}
