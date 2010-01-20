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

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.IModel;

import wickettree.ITreeProvider;
import wickettree.NestedTree;
import de.elateportal.model.ComplexTaskDef;

/**
 * @author Steffen Dienst
 * 
 */
public class ComplexTaskDefTree extends NestedTree {

  private IModel<ComplexTaskDef> currentTaskdef;
  private IModel<?> selectedModel;

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
    if (isComplextask(selected)) {
      return selected;
    }

    final ComplexTaskdefTreeProvider prov = (ComplexTaskdefTreeProvider) getProvider();
    final Iterator<?> it = prov.getRoots();
    while (it.hasNext()) {
      final IModel<?> root = prov.model(it.next());
      final IModel<?> ctdModel = findTaskDefThatContains(selected, root);
      if (ctdModel != null) {
        return ctdModel;
      }
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
    if (currentNode.equals(selected)) {
      return currentNode;
    } else if (provider.hasChildren(currentNode.getObject())) {
      final Iterator childrenIterator = provider.getChildren(currentNode.getObject());
      while (childrenIterator.hasNext()) {
        final IModel<?> inSubtree = findTaskDefThatContains(selected, provider.model(childrenIterator.next()));
        if (inSubtree != null) {
          if (inSubtree.getObject().getClass().equals(ComplexTaskDef.class)) {
            return inSubtree;
          } else {
            return currentNode;
          }
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
