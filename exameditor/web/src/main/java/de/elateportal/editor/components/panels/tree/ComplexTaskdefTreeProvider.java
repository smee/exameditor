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
import java.util.List;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.jvnet.hyperjaxb3.item.Item;

import wickettree.ITreeProvider;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

import de.elateportal.model.ComplexTaskDef;
import de.elateportal.model.TaskBlockType;
import de.elateportal.model.ComplexTaskDef.Category;
import de.elateportal.model.ComplexTaskDef.Category.ClozeTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.MappingTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.McTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.McTaskBlockOrClozeTaskBlockOrTextTaskBlockItem;
import de.elateportal.model.ComplexTaskDef.Category.PaintTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.TextTaskBlock;

/**
 * @author Steffen Dienst
 * 
 */
public class ComplexTaskdefTreeProvider implements ITreeProvider<Object> {
    final static Function<Item, Object> itemGetter = new Function<Item, Object>() {

        public Object apply(final Item item) {
            return item.getItem();
        }
    };

    private final IModel<List<ComplexTaskDef>> model;

    public ComplexTaskdefTreeProvider(final IModel<List<ComplexTaskDef>> model) {
        this.model = model;
    }

    public IModel createLabelModel(final IModel<?> model) {
        return model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    public void detach() {
        model.detach();
    }

    private Iterator<TaskBlockType> getChildren(final Category cat) {
        return Iterators.transform(cat.getMcTaskBlockOrClozeTaskBlockOrTextTaskBlockItems().iterator(),
                new Function<McTaskBlockOrClozeTaskBlockOrTextTaskBlockItem, TaskBlockType>() {

            public TaskBlockType apply(final McTaskBlockOrClozeTaskBlockOrTextTaskBlockItem block) {
                return block.getItem();
            }
        });
    }

    private Iterator<Object> getChildren(final ClozeTaskBlock tb) {
        return Iterators.transform(tb.getClozeSubTaskDefOrChoiceItems().iterator(), itemGetter);
    }

    private Iterator<Category> getChildren(final ComplexTaskDef ctd) {
        return ctd.getCategory().iterator();
    }

    private Iterator<Object> getChildren(final MappingTaskBlock tb) {
        return Iterators.transform(tb.getMappingSubTaskDefOrChoiceItems().iterator(), itemGetter);
    }

    private Iterator<Object> getChildren(final McTaskBlock tb) {
        return Iterators.transform(tb.getMcSubTaskDefOrChoiceItems().iterator(), itemGetter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see wickettree.ITreeProvider#getChildren(java.lang.Object)
     */
    public Iterator<? extends Object> getChildren(final Object object) {
        if (object instanceof ComplexTaskDef) {
            return getChildren((ComplexTaskDef) object);
        } else if (object instanceof Category) {
            return getChildren((Category) object);
        } else if (object instanceof McTaskBlock) {
            return getChildren((McTaskBlock) object);
        } else if (object instanceof ClozeTaskBlock) {
            return getChildren((ClozeTaskBlock) object);
        } else if (object instanceof MappingTaskBlock) {
            return getChildren((MappingTaskBlock) object);
        } else if (object instanceof TextTaskBlock) {
            return getChildren((TextTaskBlock) object);
        } else if (object instanceof PaintTaskBlock) {
            return getChildren((PaintTaskBlock) object);
        } else {
            return Iterators.emptyIterator();
        }
    }

    private Iterator<Object> getChildren(final PaintTaskBlock tb) {
        return Iterators.transform(tb.getPaintSubTaskDefOrChoiceItems().iterator(), itemGetter);
    }

    private Iterator<Object> getChildren(final TextTaskBlock tb) {
        return Iterators.transform(tb.getTextSubTaskDefOrChoiceItems().iterator(), itemGetter);
    }

    public Iterator<? extends Object> getRoots() {
        return model.getObject().iterator();
    }

    public boolean hasChildren(final Object object) {
        return Iterators.size(getChildren(object)) > 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see wickettree.ITreeProvider#model(java.lang.Object)
     */
    public IModel<Object> model(final Object object) {
        return new CompoundPropertyModel<Object>(object) {
            @Override
            public boolean equals(final Object obj) {
                final Object myObj = getObject();
                if (myObj != null && obj == null) {
                    return false;
                }
                final Object otherObj = ((IModel) obj).getObject();
                if (myObj == null) {
                    return otherObj==null;
                } else {
                    return myObj.equals(otherObj);
                }
            }

            @Override
            public int hashCode() {
                return getObject().hashCode();
            }
        };
    }

}
