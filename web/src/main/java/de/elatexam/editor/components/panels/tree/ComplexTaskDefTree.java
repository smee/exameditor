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
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.CssResourceReference;

import wickettree.ITreeProvider;
import wickettree.NestedTree;

import com.google.common.collect.ImmutableMap;

import de.elatexam.editor.user.BasicUser;
import de.elatexam.model.Category;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.Indexed;
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
public class ComplexTaskDefTree<T extends Indexed> extends NestedTree<T> {
  final static ImmutableMap<Class<?>, String[]> tranferTypes = new ImmutableMap.Builder<Class<?>, String[]>()
      .put(MappingTaskBlock.class, new String[]{"mapping", "category"})
      .put(MappingSubTaskDef.class, new String[]{"mapping"})

      .put(McTaskBlock.class, new String[]{"mc", "category"})
      .put(McSubTaskDef.class, new String[]{"mc"})

      .put(ClozeTaskBlock.class, new String[]{"cloze", "category"})
      .put(ClozeSubTaskDef.class, new String[]{"cloze"})

      .put(PaintTaskBlock.class, new String[]{"paint", "category"})
      .put(PaintSubTaskDef.class, new String[]{"paint"})

      .put(TextTaskBlock.class, new String[]{"text", "category"})
      .put(TextSubTaskDef.class, new String[]{"text"})
      .put(Category.class, new String[]{"category"})
  	  .put(BasicUser.class,new String[]{"user"})
  	  .put(ComplexTaskDef.class,new String[]{"complextask"})
      .build();
  final static ImmutableMap<Class<?>, String> dragStarts = new ImmutableMap.Builder<Class<?>, String>()
      .put(MappingSubTaskDef.class, "a.tree-mapping.subtaskdef")
      .put(McSubTaskDef.class, "a.tree-mc.subtaskdef")
      .put(ClozeSubTaskDef.class, "a.tree-cloze.subtaskdef")
      .put(PaintSubTaskDef.class, "a.tree-paint.subtaskdef")
      .put(TextSubTaskDef.class, "a.tree-text.subtaskdef")
      .put(MappingTaskBlock.class, "a.tree-mapping.taskblock")
      .put(McTaskBlock.class, "a.tree-mc.taskblock")
      .put(ClozeTaskBlock.class, "a.tree-cloze.taskblock")
      .put(PaintTaskBlock.class, "a.tree-paint.taskblock")
      .put(TextTaskBlock.class, "a.tree-text.taskblock")
      .build();



  private IModel<ComplexTaskDef> currentTaskdef;
  private IModel<T> selectedModel;

  /**
   * @param id
   * @param provider
   */
  public ComplexTaskDefTree(final String id, final ComplexTaskdefTreeProvider provider) {
    super(id, provider);

  }

  /* (non-Javadoc)
   * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
   */
  @Override
  public void renderHead(IHeaderResponse response) {
    super.renderHead(response);
    response.renderCSSReference(new CssResourceReference(ComplexTaskDefTree.class, "theme/theme.css"));
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

  /* (non-Javadoc)
   * @see wickettree.AbstractTree#newContentComponent(java.lang.String, org.apache.wicket.model.IModel)
   */
  @Override
  protected Component newContentComponent(final String id, final IModel model) {
    return new TaskTreeElement(id, this, model);
  }

  /**
   * Inform the tree about the selection. This is needed to be able to fire a {@link TreeSelectionEvent}.
   *
   * @param model
   * @param tree2
   * @param target
   */
  void select(final IModel<T> model, final AjaxRequestTarget target) {
    if (selectedModel != null && selectedModel.getObject() != null) {
      // redraw the now deselected node
      updateNode(selectedModel.getObject(), target);
      selectedModel.detach();
      selectedModel = null;
    }
    selectedModel = model;
    updateNode(model.getObject(), target);
    this.currentTaskdef = (IModel<ComplexTaskDef>) findCurrentTaskDef(model);
    
    send(getPage(),Broadcast.BREADTH,target);
  }


  /**
   * traverse parents to find taskdef predecessor for every object!
   *
   * @param selected
   */
  private IModel<T> findCurrentTaskDef(final IModel<T> selected) {
    if (isComplextask(selected))
      return selected;

    final ITreeProvider<T> prov = getProvider();
    final Iterator<? extends T> it = prov.getRoots();
    while (it.hasNext()) {
      final IModel<T> root = prov.model(it.next());
      final IModel<T> ctdModel = findTaskDefThatContains(selected, root);
      if (ctdModel != null)
        return ctdModel;
    }
    return null;
  }

  /**
   * @param selected
   * @return
   */
  private boolean isComplextask(final IModel<T> selected) {
    return selected.getObject().getClass().equals(ComplexTaskDef.class);
  }

  private IModel<T> findTaskDefThatContains(final IModel<T> selected, final IModel<T> currentNode) {
    final ITreeProvider<T> provider = getProvider();
    if (currentNode.equals(selected))
      return currentNode;
    else if (provider.hasChildren(currentNode.getObject())) {
      final Iterator<? extends T> childrenIterator = provider.getChildren(currentNode.getObject());
      while (childrenIterator.hasNext()) {
        final IModel<T> inSubtree = findTaskDefThatContains(selected, provider.model(childrenIterator.next()));
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
  public IModel<T> getSelected() {
    return selectedModel;
  }

  /* (non-Javadoc)
   * @see org.apache.wicket.Component#onEvent(org.apache.wicket.event.IEvent)
   */
  @Override
  public void onEvent(IEvent<?> event) {
    super.onEvent(event);
    if(event.getPayload() instanceof AjaxRequestTarget)
      ((AjaxRequestTarget) event.getPayload()).add(this);
  }

}
