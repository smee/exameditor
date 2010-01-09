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
package de.elateportal.editor.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.databinder.auth.data.DataPassword;
import net.databinder.auth.data.DataUser;
import net.databinder.auth.data.hib.BasicPassword;

import org.apache.wicket.authorization.strategies.role.Roles;
import org.hibernate.annotations.CollectionOfElements;

import de.elateportal.model.ComplexTaskDef;
import de.elateportal.model.SubTaskDefType;

/**
 * @author Steffen Dienst
 * 
 */
@Entity
public class BasicUser implements DataUser {
	private BasicPassword password;

	@Column(unique = true)
	private String username;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@CollectionOfElements(targetElement = String.class)
	private final Set<String> roles;

	@CollectionOfElements(targetElement = ComplexTaskDef.class)
	private final List<ComplexTaskDef> taskdefs;
	@CollectionOfElements(targetElement = SubTaskDefType.class)
	private final List<SubTaskDefType> subtaskdefs;

	public BasicUser() {
		subtaskdefs = new ArrayList<SubTaskDefType>();
		taskdefs = new ArrayList<ComplexTaskDef>();

		roles = new Roles(Roles.USER);
		password = new BasicPassword();
	}

	public void addRole(String role) {
		this.roles.add(role);
	}

	public Integer getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.databinder.auth.data.DataUser#getPassword()
	 */
	public DataPassword getPassword() {
		return password;
	}

	public Set<String> getRoles() {
		return new HashSet<String>(roles);
	}

	public List<SubTaskDefType> getSubtaskdefs() {
		return subtaskdefs;
	}

	public List<ComplexTaskDef> getTaskDefs() {
		return taskdefs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.databinder.auth.data.DataUser#getUsername()
	 */
	public String getUsername() {
		return username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.databinder.auth.data.DataUser#hasRole(java.lang.String)
	 */
	public boolean hasRole(String role) {
		return roles.contains(role);
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPassword(BasicPassword password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
