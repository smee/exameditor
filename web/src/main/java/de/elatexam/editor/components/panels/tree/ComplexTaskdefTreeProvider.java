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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.databinder.models.hib.HibernateObjectModel;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

import wickettree.ITreeProvider;

import com.google.common.collect.Iterators;

import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.util.Stuff;
import de.elatexam.model.Category;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.Indexed;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TaskBlock;

/**
 * Provide tree structure for rendering. Represents a {@link ComplexTaskDef} in a user comprehensible way. <br>
 * TODO add taskdef choice blocks
 *
 * @author Steffen Dienst
 *
 */
public class ComplexTaskdefTreeProvider implements ITreeProvider<Object> {

    /**
     * wrap each object in a HibernateObjectModel, implement equals/hashcode correctly (respecting the primary key only)
     *
     * @author Steffen Dienst
     *
     */
    private static class HackedHibernateObjectModel extends HibernateObjectModel implements IWrapModel, IComponentInheritedModel {
        private HackedHibernateObjectModel(Class objectClass, Serializable entityId) {
            super(objectClass, entityId);
        }

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
            return ((Indexed) o).getHjid();
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

        // HACK: the tree uses StyledLinks which itself contains an AjaxFallBackLink without any model
        // This breaks the DND-feature somehow. Using these two methods we signal
        // that children may inherit this model if they have none
        @Override
        public IWrapModel wrapOnInheritance(Component component) {
            return this;
        }

        @Override
        public IModel getWrappedModel() {
            return this;
        }
    }

private final IModel<List<?>> model;

  public ComplexTaskdefTreeProvider(final IModel<List<?>> model) {
    this.model = model;
  }

    // public IModel createLabelModel(final IModel<?> model) {
    // return model;
    // }

  /*
   * (non-Javadoc)
   *
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {
    model.detach();
  }

    private Iterator<TaskBlock> getChildren(final Category cat) {
        return cat.getTaskBlocks().iterator();
  }

    private Iterator<Object> getChildren(final TaskBlock tb) {
        List<Object> subtasks = new ArrayList<Object>(Stuff.getSubtaskDefs(tb));
        Collections.sort(subtasks, new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                return ((SubTaskDef) o1).getXmlid().compareTo(((SubTaskDef) o2).getXmlid());
            }
        });
        return subtasks.iterator();
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

      return new HackedHibernateObjectModel(object.getClass(), (Serializable) PropertyUtils.getProperty(object, "hjid"));
    } catch (final Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
