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

import de.elateportal.editor.user.BasicUser;
import de.elateportal.model.Category;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.ComplexTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.TextSubTaskDef;
import de.elateportal.model.Category.ClozeTaskBlock;
import de.elateportal.model.Category.MappingTaskBlock;
import de.elateportal.model.Category.McTaskBlock;
import de.elateportal.model.Category.PaintTaskBlock;
import de.elateportal.model.Category.TextTaskBlock;

/**
 * @author Steffen Dienst
 * 
 */
public class TaskTreeElement<T> extends StyledLinkLabel<T> {
  final static ImmutableMap<Class<?>, String> expressions = new ImmutableMap.Builder<Class<?>, String>()

  .put(BasicUser.class, "username")
  .put(ComplexTaskDef.class, "title")
  .put(Category.class, "title")
  .put(McTaskBlock.class, "class.simpleName")
  .put(MappingTaskBlock.class, "class.simpleName")
  .put(ClozeTaskBlock.class, "class.simpleName")
  .put(TextTaskBlock.class, "class.simpleName")
  .put(PaintTaskBlock.class, "class.simpleName")
  .put(McSubTaskDef.class, "xmlid")
  .put(MappingSubTaskDef.class, "xmlid")
  .put(ClozeSubTaskDef.class, "xmlid")
  .put(TextSubTaskDef.class, "xmlid")
  .put(PaintSubTaskDef.class, "xmlid")
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

  public TaskTreeElement(final String id, final ComplexTaskDefTree tree, final IModel<T> model) {
    super(id, model);

    this.tree = tree;
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
  protected String getOtherStyleClass(final T t) {
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
    // TODO add error icons for invalid model elements
    final T t = getModelObject();

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
    final String configuredClass = styleClasses.get(t.getClass());
    if (configuredClass != null) {
      styleClass = configuredClass;
    }
    if (getModel().equals(tree.getSelected())) {
      styleClass += " " + getSelectedStyleClass();
    }

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
  protected IModel<String> newLabelModel(final IModel<T> model) {
    final Object o = model.getObject();
    return new PropertyModel<String>(model, expressions.get(o.getClass()));
  }

  /**
   * Toggle the node's {@link State} on click.
   */
  @Override
  protected void onClick(final AjaxRequestTarget target) {
    final T t = getModelObject();
    if (tree.getState(t) == State.COLLAPSED) {
      tree.expand(t);
    }
    tree.select(getModel(), target);
  }
}
