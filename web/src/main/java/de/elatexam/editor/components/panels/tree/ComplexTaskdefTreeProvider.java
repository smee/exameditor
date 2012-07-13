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

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;

import wickettree.ITreeProvider;

import com.google.common.collect.Iterators;

import de.elatexam.editor.user.BasicUser;
import de.elatexam.model.Category;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.Indexed;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.manual.HomogeneousTaskBlock;

/**
 * Provide tree structure for rendering. Represents a {@link ComplexTaskDef} in
 * a user comprehensible way. <br>
 * TODO add taskdef choice blocks
 * 
 * @author Steffen Dienst
 * 
 */
public class ComplexTaskdefTreeProvider<T extends Indexed> implements ITreeProvider<T> {

	/**
	 * wrap each object in a HibernateObjectModel, implement equals/hashcode
	 * correctly (respecting the primary key only)
	 * 
	 * @author Steffen Dienst
	 * 
	 */
	private static class HackedHibernateObjectModel<T extends Indexed>
			extends
				HibernateObjectModel<T>
			implements
				IWrapModel<T>,
				IComponentInheritedModel<T> {
		
		private HackedHibernateObjectModel(Class<? extends Indexed> objectClass, Serializable entityId) {
			super(objectClass, entityId);
		}

		@Override
		public boolean equals(final Object obj) {
			final T target = getObject();
			if (target != null && obj instanceof HibernateObjectModel)
				return equalId(target,((HibernateObjectModel<T>) obj).getObject());
			return super.equals(obj);
		}

		private boolean equalId(final T o1, final T o2) {
			return o1.getClass().equals(o2.getClass())
					&& o1.getHjid().equals(o2.getHjid());
		}

		@Override
		public int hashCode() {
			final T target = getObject();
			if (target == null)
				return super.hashCode();
			int hash = 1;
			hash = hash * 31 + target.getClass().hashCode();
			hash = hash * 31 + target.getHjid().hashCode();
			return hash;

		}

		// HACK: the tree uses StyledLinks which itself contains an
		// AjaxFallBackLink without any model
		// This breaks the DND-feature somehow. Using these two methods we
		// signal
		// that children may inherit this model if they have none
		@Override
		public IWrapModel<T> wrapOnInheritance(Component component) {
			return this;
		}

		@Override
		public IModel<T> getWrappedModel() {
			return this;
		}
	}

	private final IModel<List<T>> model;

	public ComplexTaskdefTreeProvider(final IModel<List<T>> model) {
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	public void detach() {
		model.detach();
	}

	private Iterator<T> getChildren(final Category cat) {
		return (Iterator<T>) cat.getTaskBlocks().iterator();
	}

	private Iterator<T> getChildren(final HomogeneousTaskBlock tb) {
		List<? extends SubTaskDef> subtasks = tb.getSubtaskDefs();
		return (Iterator<T>) subtasks.iterator();
	}

	private Iterator<T> getChildren(final ComplexTaskDef ctd) {
		return (Iterator<T>) ctd.getCategory().iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wickettree.ITreeProvider#getChildren(java.lang.Object)
	 */
	public Iterator<T> getChildren(final Indexed object) {
		if (object instanceof BasicUser)
			return getChildren((BasicUser) object);
		else if (object instanceof ComplexTaskDef)
			return getChildren((ComplexTaskDef) object);
		else if (object instanceof Category)
			return getChildren((Category) object);
		else if (object instanceof HomogeneousTaskBlock)
			return getChildren((HomogeneousTaskBlock) object);
		else
			return Iterators.emptyIterator();
	}

	private Iterator<T> getChildren(final BasicUser user) {
		return (Iterator<T>) user.getTaskdefs().iterator();
	}
	public Iterator<T> getRoots() {
		return model.getObject().iterator();
	}

	public boolean hasChildren(final Indexed object) {
		return Iterators.size(getChildren(object)) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wickettree.ITreeProvider#model(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public IModel<T> model(final T object) {
		try {

			return new HackedHibernateObjectModel<T>(object.getClass(),object.getHjid());
		} catch (final Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
