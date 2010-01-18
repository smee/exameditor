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
package de.elateportal.editor.pages;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.databinder.auth.hib.AuthDataSession;
import net.databinder.hib.Databinder;
import net.databinder.models.hib.CriteriaFilterAndSort;
import net.databinder.models.hib.OrderingCriteriaBuilder;
import net.databinder.models.hib.SortableHibernateProvider;

import org.apache.wicket.extensions.markup.html.repeater.util.SingleSortState;
import org.hibernate.Query;
import org.hibernate.Session;

import de.elateportal.editor.util.RemoveNullResultTransformer;
import de.elateportal.model.SubTaskDefType;

/**
 * XXX ugly hack, clean up this mess as soon as possible. Assumes just one sort,
 * no nested sort properties, no nested filtering properties, concrete classes
 * etc.
 * 
 * @author Steffen Dienst
 * 
 * @param <T>
 */
final class PrivateSubtasksDataProvider<T> extends SortableHibernateProvider<T> {
  private final CriteriaFilterAndSort builder;
  private final Class<T> clazz;
  final String query = "select task from BasicUser user left join user.subtaskdefs task where user.username='%s'";
  final String classQuery = "and task.class in (%s)";

  PrivateSubtasksDataProvider(final Class<T> objectClass, final OrderingCriteriaBuilder criteriaBuilder,
      final CriteriaFilterAndSort builder, final Class<T> clazz) {
    super(objectClass, criteriaBuilder);
    this.builder = builder;
    this.clazz = clazz;
  }

  private String createQueryString() {
    String q = String.format(query, AuthDataSession.get().getUser().getUsername());
    if (!SubTaskDefType.class.equals(clazz)) {
      q = q + String.format(classQuery, clazz.getName());
    }
    for (final Map.Entry<String, String> entry : (Set<Map.Entry<String, String>>) ((Map) builder.getFilterState()).entrySet()) {
      final String property = entry.getKey();
      final String value = entry.getValue();
      if (value == null) {
        continue;
      }
      q = q + " and task." + property + " like '%" + value + "%'";
    }
    final SingleSortState sort = (SingleSortState) getSortState();
    if (sort.getSort() != null) {
      q = q + " order by task." + sort.getSort().getProperty() + " " + (sort.getSort().isAscending() ? " asc" : "desc");
    }
    return q;
  }

  @Override
  public Iterator<T> iterator(final int first, final int count) {
    final Session sess = Databinder.getHibernateSession(getFactoryKey());

    final Query q = sess.createQuery(createQueryString());
    q.setFirstResult(first);
    q.setMaxResults(count);
    // q.setResultTransformer(RemoveNullResultTransformer.INSTANCE);
    return q.iterate();
  }

  @Override
  public int size() {
    final Session sess = Databinder.getHibernateSession(getFactoryKey());
    final Query q = sess.createQuery(createQueryString());
    q.setResultTransformer(RemoveNullResultTransformer.INSTANCE);
    final int size = q.list().size();
    return size;
  }
}