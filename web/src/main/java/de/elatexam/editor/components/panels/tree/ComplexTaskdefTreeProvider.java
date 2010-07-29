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

import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.util.Stuff;
import de.elatexam.model.Category;
import de.elatexam.model.Category.CategoryTaskBlocksItem;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.TaskBlock;

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

    private Iterator<TaskBlock> getChildren(final Category cat) {
    return Iterators.transform(cat.getTaskBlocksItems().iterator(),
                new Function<CategoryTaskBlocksItem, TaskBlock>() {

      public TaskBlock apply(final CategoryTaskBlocksItem block) {
        return block.getItem();
      }
    });
  }

    private Iterator<Object> getChildren(final TaskBlock tb) {
    return Iterators.transform(Stuff.getItems(tb).iterator(), itemGetter);
  }

  private Iterator<Category> getChildren(final ComplexTaskDef ctd) {
    return ctd.getCategory().iterator();
  }



  /*
   * (non-Javadoc)
   *
   * @see wickettree.ITreeProvider#getChildren(java.lang.Object)
   */
  public Iterator<? extends Object> getChildren(final Object object) {
    if (object instanceof BasicUser)
        return getChildren((BasicUser) object);
    else if (object instanceof ComplexTaskDef)
        return getChildren((ComplexTaskDef) object);
    else if (object instanceof Category)
        return getChildren((Category) object);
        else if (object instanceof TaskBlock)
        return getChildren((TaskBlock) object);
    else
        return Iterators.emptyIterator();
  }


    private Iterator<?> getChildren(final BasicUser user) {
    return user.getTaskdefs().iterator();
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
            // wrap each object in a HibernateObjectModel, implement equals/hashcode correctly (respecting the primary
            // key only)
      return new HibernateObjectModel(object.getClass(), (Serializable) PropertyUtils.getProperty(object, "hjid")) {
        @Override
        public boolean equals(final Object obj) {
          final Object target = getObject();
          if (target != null && obj instanceof HibernateObjectModel)
            return equalId(target, ((HibernateObjectModel) obj).getObject());
          return super.equals(obj);
        }

        private boolean equalId(final Object o1, final Object o2) {
          return o1.getClass().equals(o2.getClass()) && getId(o1).equals(getId(o2));
        }

        private Object getId(final Object o) {
          try {
            return PropertyUtils.getProperty(o, "hjid");
          } catch (final Exception e) {
            throw new RuntimeException(e);
          }
        }

        @Override
        public int hashCode() {
          final Object target = getObject();
          if (target == null)
            return super.hashCode();
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
