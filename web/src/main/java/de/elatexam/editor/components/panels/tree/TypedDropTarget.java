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
package de.elatexam.editor.components.panels.tree;

import org.apache.wicket.ajax.AjaxRequestTarget;

import com.google.common.collect.ImmutableMap;

import de.elatexam.model.Category;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.MappingTaskBlock;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.McTaskBlock;
import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.PaintTaskBlock;
import de.elatexam.model.TextSubTaskDef;
import de.elatexam.model.TextTaskBlock;

import wicketdnd.DropTarget;
import wicketdnd.Location;
import wicketdnd.Operation;
import wicketdnd.Reject;
import wicketdnd.Transfer;

class TypedDropTarget extends DropTarget {
	  final static ImmutableMap<Class<?>, String> dropTaskblocks = new ImmutableMap.Builder<Class<?>, String>()
      .put(MappingSubTaskDef.class, "a.tree-mapping.taskblock")
      .put(McSubTaskDef.class, "a.tree-mc.taskblock")
      .put(ClozeSubTaskDef.class, "a.tree-cloze.taskblock")
      .put(PaintSubTaskDef.class, "a.tree-paint.taskblock")
      .put(TextSubTaskDef.class, "a.tree-text.taskblock")
      .put(Category.class, "a.category")
      .build();
	  
    private final String[] types;
    private final ComplexTaskDefTree tree;

    public TypedDropTarget(Class<?> type, ComplexTaskDefTree tree, Operation... operations) {
      super(operations);
      this.tree = tree;
      this.types = ComplexTaskDefTree.tranferTypes.get(type);
      this.dropTopAndBottom(ComplexTaskDefTree.dragStarts.get(type));
      this.dropCenter(dropTaskblocks.get(type));
    }

    /* (non-Javadoc)
     * @see wicketdnd.DropTarget#getTypes()
     */
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