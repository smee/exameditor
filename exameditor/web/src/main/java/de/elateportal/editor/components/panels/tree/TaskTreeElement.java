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
package de.elateportal.editor.components.panels.tree;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import wickettree.AbstractTree;
import wickettree.ITreeProvider;
import wickettree.AbstractTree.State;
import wickettree.content.StyledLinkLabel;

import com.google.common.collect.ImmutableMap;

import de.elateportal.editor.pages.TaskDefPage;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.ComplexTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.SubTaskDefType;
import de.elateportal.model.TextSubTaskDef;
import de.elateportal.model.ComplexTaskDef.Category;
import de.elateportal.model.ComplexTaskDef.Category.ClozeTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.MappingTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.McTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.PaintTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.TextTaskBlock;

/**
 * @author Steffen Dienst
 * 
 */
public class TaskTreeElement<T> extends StyledLinkLabel<T> {
	final static ImmutableMap<Class<?>, String> expressions = new ImmutableMap.Builder<Class<?>, String>()

	.put(ComplexTaskDef.class, "title")
	    .put(Category.class, "title")
	    .put(McTaskBlock.class, "class.simpleName")
	    .put(MappingTaskBlock.class, "class.simpleName")
	    .put(ClozeTaskBlock.class, "class.simpleName")
	    .put(TextTaskBlock.class, "class.simpleName")
	    .put(PaintTaskBlock.class, "class.simpleName")
	    .put(McSubTaskDef.class, "id")
	    .put(MappingSubTaskDef.class, "id")
	    .put(ClozeSubTaskDef.class, "id")
	    .put(TextSubTaskDef.class, "id")
	    .put(PaintSubTaskDef.class, "id")
	    .build();
	final static ImmutableMap<Class<?>, String> styleClasses = new ImmutableMap.Builder<Class<?>, String>()
	    .put(ComplexTaskDef.class, "tree-exam")
	    .put(McTaskBlock.class, "tree-mc")
	    .put(MappingTaskBlock.class, "tree-mapping")
	    .put(ClozeTaskBlock.class, "tree-cloze")
	    .put(TextTaskBlock.class, "tree-text")
	    .put(PaintTaskBlock.class, "tree-paint")
	    .put(McSubTaskDef.class, "tree-mc")
	    .put(MappingSubTaskDef.class, "tree-mapping")
	    .put(ClozeSubTaskDef.class, "tree-cloze")
	    .put(TextSubTaskDef.class, "tree-text")
	    .put(PaintSubTaskDef.class, "tree-paint")

	.build();

	private final ComplexTaskDefTree tree;
	private final TaskDefPage page;

	public TaskTreeElement(String id, ComplexTaskDefTree tree, TaskDefPage page, IModel<T> model) {
		super(id, model);

		this.tree = tree;
		this.page = page;
	}

	protected String getClosedStyleClass() {
		return "tree-folder-closed";
	}

	protected String getOpenStyleClass() {
		return "tree-folder-open";
	}

	/**
	 * Get a style class for anything other than closed or open folders.
	 */
	protected String getOtherStyleClass(T t) {
		return "tree-folder-other";
	}

	/**
	 * Get a style class to render for a selected folder.
	 * 
	 * @see #isSelected()
	 */
	protected String getSelectedStyleClass() {
		return "selected";
	}

	/**
	 * Delegates to others methods depending wether the given model is a folder,
	 * expanded, collapsed or selected.
	 * 
	 * @see ITreeProvider#hasChildren(Object)
	 * @see AbstractTree#getState(Object)
	 * @see #isSelected()
	 * @see #getOpenStyleClass()
	 * @see #getClosedStyleClass()
	 * @see #getOtherStyleClass(Object)
	 * @see #getSelectedStyleClass()
	 */
	@Override
	protected String getStyleClass() {
		T t = getModelObject();

		String styleClass;
		if (tree.getProvider().hasChildren(t)) {
			if (tree.getState(t) == State.EXPANDED) {
				styleClass = getOpenStyleClass();
			} else {
				styleClass = getClosedStyleClass();
			}
		} else {
			styleClass = getOtherStyleClass(t);
		}
		// overwrite syle class for specific model classes (uses custom icons)
		String configuredClass = styleClasses.get(t.getClass());
		if (configuredClass != null)
			styleClass = configuredClass;

		return styleClass;
	}

	/**
	 * Always clickable.
	 * 
	 * @see ITreeProvider#hasChildren(Object)
	 */
	@Override
	protected boolean isClickable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wickettree.content.StyledLinkLabel#newLabelModel(org.apache.wicket.model
	 * .IModel)
	 */
	@Override
	protected IModel<String> newLabelModel(IModel<T> model) {
		Object o = model.getObject();
		if (o instanceof ComplexTaskDef) {

		}
		return new PropertyModel<String>(model, expressions.get(o.getClass()));
	}

	/**
	 * Toggle the node's {@link State} on click.
	 */
	@Override
	protected void onClick(AjaxRequestTarget target) {
		T t = getModelObject();
		if (tree.getState(t) == State.EXPANDED) {
			tree.collapse(t);
		} else {
			tree.expand(t);
		}
		if (t instanceof SubTaskDefType) {
			page.renderPanelFor((SubTaskDefType) t, target);
		}
		if (t instanceof ComplexTaskDef) {
			tree.setCurrentTaskdef((ComplexTaskDef) t);
			page.renderPanelFor((ComplexTaskDef) t, target);
		}
	}
}
