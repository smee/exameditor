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

import net.databinder.models.hib.CriteriaBuilder;
import net.databinder.models.hib.HibernateObjectModel;

import org.hibernate.Criteria;

import de.elateportal.editor.components.panels.tree.ComplexTaskDefTree;
import de.elateportal.editor.components.panels.tree.ComplexTaskdefTreeProvider;
import de.elateportal.model.ComplexTaskDef;

/**
 * @author Steffen Dienst
 * 
 */
public class TaskDefPage extends OverviewPage {

	public TaskDefPage() {
		HibernateObjectModel<ComplexTaskDef> taskdefModel = new HibernateObjectModel<ComplexTaskDef>(ComplexTaskDef.class,
		    new CriteriaBuilder() {

			    public void build(Criteria criteria) {
				    criteria.setMaxResults(1);
			    }
		    });
		add(new ComplexTaskDefTree("tree", new ComplexTaskdefTreeProvider(taskdefModel)));
	}
}
