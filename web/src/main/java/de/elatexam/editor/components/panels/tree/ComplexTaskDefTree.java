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

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.IModel;

import wicketdnd.DragSource;
import wicketdnd.DropTarget;
import wicketdnd.Location;
import wicketdnd.Operation;
import wicketdnd.Reject;
import wicketdnd.Transfer;
import wickettree.ITreeProvider;
import wickettree.NestedTree;

import com.google.common.collect.ImmutableMap;

import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.MappingTaskBlock;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.McTaskBlock;
import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.PaintTaskBlock;
import de.elatexam.model.TextSubTaskDef;
import de.elatexam.model.TextTaskBlock;

/**
 * @author Steffen Dienst
 *
 */
public class ComplexTaskDefTree extends NestedTree {
  final static ImmutableMap<Class<?>, String> tranferTypes = new ImmutableMap.Builder<Class<?>, String>()
      .put(MappingTaskBlock.class, "mapping")
      .put(MappingSubTaskDef.class, "mapping")

      .put(McTaskBlock.class, "mc")
      .put(McSubTaskDef.class, "mc")

      .put(ClozeTaskBlock.class, "cloze")
      .put(ClozeSubTaskDef.class, "cloze")

      .put(PaintTaskBlock.class, "paint")
      .put(PaintSubTaskDef.class, "paint")

      .put(TextTaskBlock.class, "text")
      .put(TextSubTaskDef.class, "text")
      .build();
  final static ImmutableMap<Class<?>, String> dragStarts = new ImmutableMap.Builder<Class<?>, String>()
      .put(MappingSubTaskDef.class, "a.tree-mapping.subtaskdef")
      .put(McSubTaskDef.class, "a.tree-mc.subtaskdef")
      .put(ClozeSubTaskDef.class, "a.tree-cloze.subtaskdef")
      .put(PaintSubTaskDef.class, "a.tree-paint.subtaskdef")
      .put(TextSubTaskDef.class, "a.tree-text.subtaskdef")
      .build();
  final static ImmutableMap<Class<?>, String> dropTaskblocks = new ImmutableMap.Builder<Class<?>, String>()
      .put(MappingSubTaskDef.class, "a.tree-mapping.taskblock")
      .put(McSubTaskDef.class, "a.tree-mc.taskblock")
      .put(ClozeSubTaskDef.class, "a.tree-cloze.taskblock")
      .put(PaintSubTaskDef.class, "a.tree-paint.taskblock")
      .put(TextSubTaskDef.class, "a.tree-text.taskblock")
      .build();

  /**
   * Preconfigured {@link DragSource} that starts for one subtaskdef type only.
   *
   * @author Steffen Dienst
   *
   */
  static class TypedDragSource extends DragSource {

    private final String[] types;

    public TypedDragSource(Class<?> type, Operation... operations) {
      super(operations);
      this.types = new String[] { tranferTypes.get(type) };
      drag(dragStarts.get(type));
    }

    @Override
    public String[] getTypes() {
      return types;
    }
  }

  static class TypedDropTarget extends DropTarget {
    private final String[] types;
    private final ComplexTaskDefTree tree;

    public TypedDropTarget(Class<?> type, ComplexTaskDefTree tree, Operation... operations) {
      super(operations);
      this.tree = tree;
      this.types = new String[] { tranferTypes.get(type) };
      this.dropTopAndBottom(dragStarts.get(type));
      this.dropCenter(dropTaskblocks.get(type));
    }

    @Override
    public String[] getTypes() {
      return types;
    }

    @Override
    public void onDrop(AjaxRequestTarget target, Transfer transfer, Location location) throws Reject {
      if (transfer == null || location == null)
        return;

      Object droppedObject = transfer.getData();
      Object droppedOn = location.getModelObject();
      // System.out.println("DropTarget#onDrop: dropping " + droppedObject + " on " + droppedOn);

      if (new ComplexTaskHierarchyPruner(tree.getProvider()).moveElement(droppedObject, droppedOn, location.getAnchor())) {
        target.addComponent(tree);
      } else {
        transfer.reject();
      }
    }

  }

  private IModel<ComplexTaskDef> currentTaskdef;
  private IModel<?> selectedModel;

  /**
   * @param id
   * @param provider
   */
  public ComplexTaskDefTree(final String id, final ComplexTaskdefTreeProvider provider) {
    super(id, provider);

    add(CSSPackageResource.getHeaderContribution(new CompressedResourceReference(ComplexTaskDefTree.class, "theme/theme.css")));

  }

  /*
   * (non-Javadoc)
   *
   * @see org.apache.wicket.Component#detachModels()
   */
  @Override
  public void detachModels() {
    super.detachModels();
    if (selectedModel != null) {
      selectedModel.detach();
    }
    if (currentTaskdef != null) {
      currentTaskdef.detach();
    }
  }

  public IModel<ComplexTaskDef> getCurrentTaskdef() {
    return currentTaskdef;
  }

  @Override
  protected Component newContentComponent(final String id, final IModel model) {
    return new TaskTreeElement(id, this, model);
  }

  /**
   * Inform the tree about the selection. This is needed to be able to call {@link #onSelect(IModel, AjaxRequestTarget)}
   *
   * @param model
   * @param tree2
   * @param target
   */
  void select(final IModel<?> model, final AjaxRequestTarget target) {
    if (selectedModel != null && selectedModel.getObject() != null) {
      // redraw the now deselected node
      updateNode(selectedModel.getObject(), target);
      selectedModel.detach();
      selectedModel = null;
    }
    selectedModel = model;
    updateNode(model.getObject(), target);
    this.currentTaskdef = (IModel<ComplexTaskDef>) findCurrentTaskDef(model);
    onSelect(selectedModel, target);
  }

  /**
   * Gets called whenever the tree selection changes.
   *
   * @param selectedModel
   * @param target
   */
  protected void onSelect(final IModel<?> selectedModel, final AjaxRequestTarget target) {
  }

  /**
   * traverse parents to find taskdef predecessor for every object!
   *
   * @param selected
   */
  private IModel<?> findCurrentTaskDef(final IModel<?> selected) {
    if (isComplextask(selected))
      return selected;

    final ComplexTaskdefTreeProvider prov = (ComplexTaskdefTreeProvider) getProvider();
    final Iterator<?> it = prov.getRoots();
    while (it.hasNext()) {
      final IModel<?> root = prov.model(it.next());
      final IModel<?> ctdModel = findTaskDefThatContains(selected, root);
      if (ctdModel != null)
        return ctdModel;
    }
    return null;
  }

  /**
   * @param selected
   * @return
   */
  private boolean isComplextask(final IModel<?> selected) {
    return selected.getObject().getClass().equals(ComplexTaskDef.class);
  }

  private IModel<?> findTaskDefThatContains(final IModel<?> selected, final IModel<?> currentNode) {
    final ITreeProvider provider = getProvider();
    if (currentNode.equals(selected))
      return currentNode;
    else if (provider.hasChildren(currentNode.getObject())) {
      final Iterator childrenIterator = provider.getChildren(currentNode.getObject());
      while (childrenIterator.hasNext()) {
        final IModel<?> inSubtree = findTaskDefThatContains(selected, provider.model(childrenIterator.next()));
        if (inSubtree != null) {
          if (inSubtree.getObject().getClass().equals(ComplexTaskDef.class))
            return inSubtree;
          else
            return currentNode;
        }
      }
    }
    return null;
  }

  /**
   * Return model of the currently selected node.
   *
   * @return
   */
  public IModel<?> getSelected() {
    return selectedModel;
  }

}
