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

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import net.databinder.models.hib.HibernateObjectModel;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.wicket.model.IModel;
import org.jvnet.hyperjaxb3.item.Item;

import wickettree.ITreeProvider;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import de.elateportal.editor.user.BasicUser;
import de.elateportal.model.Category;
import de.elateportal.model.ClozeTaskBlock;
import de.elateportal.model.ComplexTaskDef;
import de.elateportal.model.MCTaskBlock;
import de.elateportal.model.MappingTaskBlock;
import de.elateportal.model.PaintTaskBlock;
import de.elateportal.model.TaskBlockType;
import de.elateportal.model.TextTaskBlock;
import de.elateportal.model.Category.CategoryTaskBlocksItem;

/**
 * Provide tree structure for rendering. Represents a {@link ComplexTaskDef} in a user comprehensible way. <br>
 * TODO add taskdef choice blocks
 * 
 * @author Steffen Dienst
 * 
 */
public class ComplexTaskdefTreeProvider implements ITreeProvider<Object> {
  final static Function<Item, Object> itemGetter = new Function<Item, Object>() {

    public Object apply(final Item item) {
      return item.getItem();
    }
  };

  private final IModel<List<?>> model;

  public ComplexTaskdefTreeProvider(final IModel<List<?>> model) {
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
    return Iterators.transform(cat.getTaskBlocksItems().iterator(),
        new Function<CategoryTaskBlocksItem, TaskBlockType>() {

      public TaskBlockType apply(final CategoryTaskBlocksItem block) {
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

  private Iterator<Object> getChildren(final MCTaskBlock tb) {
    return Iterators.transform(tb.getMcSubTaskDefOrChoiceItems().iterator(), itemGetter);
  }

  /*
   * (non-Javadoc)
   * 
   * @see wickettree.ITreeProvider#getChildren(java.lang.Object)
   */
  public Iterator<? extends Object> getChildren(final Object object) {
    if (object instanceof BasicUser) {
      return getChildren((BasicUser) object);
    } else if (object instanceof ComplexTaskDef) {
      return getChildren((ComplexTaskDef) object);
    } else if (object instanceof Category) {
      return getChildren((Category) object);
    } else if (object instanceof MCTaskBlock) {
      return getChildren((MCTaskBlock) object);
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

  /**
   * Use {@link #findParentOf(Object)}, remove the child from it's parent's children and return the parent.
   * 
   * @param child
   * @return
   */
  public Object removeFromParent(final Object child) {
    final Object parent = findParentOf(child);
    if (parent != null) {
      if (parent instanceof BasicUser) {
        ((BasicUser) parent).getTaskdefs().remove(child);
      } else if (parent instanceof ComplexTaskDef) {
        ((ComplexTaskDef) parent).getCategory().remove(child);
      } else if (parent instanceof Category) {
        Iterators.removeAll(getChildren((Category) parent), Lists.newArrayList(child));
      } else if (parent instanceof MCTaskBlock) {
        Iterators.removeAll(getChildren(parent), Lists.newArrayList(child));
      } else if (parent instanceof ClozeTaskBlock) {
        Iterators.removeAll(getChildren((ClozeTaskBlock) parent), Lists.newArrayList(child));
      } else if (parent instanceof MappingTaskBlock) {
        Iterators.removeAll(getChildren((MappingTaskBlock) parent), Lists.newArrayList(child));
      } else if (parent instanceof TextTaskBlock) {
        Iterators.removeAll(getChildren((TextTaskBlock) parent), Lists.newArrayList(child));
      } else if (parent instanceof PaintTaskBlock) {
        Iterators.removeAll(getChildren((PaintTaskBlock) parent), Lists.newArrayList(child));
      }
    }
    return parent;
  }

  /**
   * Find the model where is true: getChildren(parent).contains(child).
   * 
   * @param child
   * @return
   */
  public Object findParentOf(final Object child) {
    final Iterator it = getRoots();
    while (it.hasNext()) {
      final Object root = it.next();
      final Object parent = findParentOf(root, child);
      if (parent != null) {
        return parent;
      }
    }
    return null;
  }

  private Object findParentOf(final Object current, final Object child) {
    final Iterator it = getChildren(current);
    while (it.hasNext()) {
      final Object potentialParent = it.next();
      if (potentialParent.equals(child)) {
        return current;
      }
      final Object parent = findParentOf(potentialParent, child);
      if (parent != null) {
        return parent;
      }
    }
    return null;
  }

  private Iterator<?> getChildren(final BasicUser user) {
    return user.getTaskdefs().iterator();
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
  @SuppressWarnings("unchecked")
  public IModel<Object> model(final Object object) {
    try {
      return new HibernateObjectModel(object.getClass(), (Serializable) PropertyUtils.getProperty(object, "hjid")) {
        @Override
        public boolean equals(final Object obj) {
          final Object target = getObject();
          if (target != null && obj instanceof HibernateObjectModel) {
            return equalId(target, ((HibernateObjectModel) obj).getObject());
          }
          return super.equals(obj);
        }

        private boolean equalId(final Object o1, final Object o2) {
          return o1.getClass().equals(o2.getClass()) && getId(o1).equals(getId(o2));
        }

        private Object getId(final Object o) {
          try {
            return PropertyUtils.getProperty(o, "hjid");
          } catch (final Exception e) {
            e.printStackTrace();
            return o.toString();
          }
        }

        @Override
        public int hashCode() {
          final Object target = getObject();
          if (target == null) {
            return super.hashCode();
          }
          int hash = 1;
          hash = hash * 31 + target.getClass().hashCode();
          hash = hash * 31 + getId(target).hashCode();
          return hash;

        }
      };
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
