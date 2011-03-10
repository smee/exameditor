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
package de.elatexam.editor.components.panels.tree;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import wicketdnd.Operation;
import wickettree.AbstractTree;
import wickettree.AbstractTree.State;
import wickettree.ITreeProvider;
import wickettree.content.StyledLinkLabel;

import com.google.common.collect.ImmutableMap;

import de.elatexam.editor.components.panels.tasks.SortableIdModel;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.model.Category;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.MappingTaskBlock;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.McTaskBlock;
import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.PaintTaskBlock;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TaskBlock;
import de.elatexam.model.TextSubTaskDef;
import de.elatexam.model.TextTaskBlock;

/**
 * @author Steffen Dienst
 *
 */
public class TaskTreeElement<T> extends StyledLinkLabel<T> {
  final static ImmutableMap<Class<?>, String> expressions = new ImmutableMap.Builder<Class<?>, String>()

  .put(ComplexTaskDef.class, "title")
  .put(Category.class, "title")
  .put(BasicUser.class, "username")
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
  .put(BasicUser.class, "tree-user")
  .put(ComplexTaskDef.class, "tree-exam")
  .put(McTaskBlock.class, "tree-mc taskblock")
  .put(MappingTaskBlock.class, "tree-mapping taskblock")
  .put(ClozeTaskBlock.class, "tree-cloze taskblock")
  .put(TextTaskBlock.class, "tree-text taskblock")
  .put(PaintTaskBlock.class, "tree-paint taskblock")
  .put(McSubTaskDef.class, "tree-mc subtaskdef")
  .put(MappingSubTaskDef.class, "tree-mapping subtaskdef")
  .put(ClozeSubTaskDef.class, "tree-cloze subtaskdef")
  .put(TextSubTaskDef.class, "tree-text subtaskdef")
  .put(PaintSubTaskDef.class, "tree-paint subtaskdef")
  .build();

  private final ComplexTaskDefTree tree;

  public TaskTreeElement(final String id, final ComplexTaskDefTree tree, final IModel<T> model) {
    super(id, model);

    this.tree = tree;

    add(new TypedDragSource(model.getObject().getClass(), Operation.MOVE));
    add(new TypedDropTarget(model.getObject().getClass(), tree, Operation.MOVE));
  }

  protected String getClosedStyleClass() {
    return "tree-folder-closed category";
  }

  protected String getOpenStyleClass() {
    return "tree-folder-open category";
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
    if (t instanceof Category) {
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
        final IModel<String> defaultModel = new PropertyModel<String>(model, expressions.get(o.getClass()));
        if (o instanceof TaskBlock)
            return Model.of("Aufgaben");
        else if (o instanceof SubTaskDef)
            return new SortableIdModel(new PropertyModel<String>(model, "xmlid"));
        return defaultModel;
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
