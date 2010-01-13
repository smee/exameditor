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
import java.util.Collection;
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

    // @CollectionOfElements(targetElement = AddonSubTaskDef.class)
    // private final List<AddonSubTaskDef> addonSubtaskdefs;
    // @CollectionOfElements(targetElement = McSubTaskDef.class)
    // private final List<McSubTaskDef> mcSubtaskdefs;
    // @CollectionOfElements(targetElement = ClozeSubTaskDef.class)
    // private final List<ClozeSubTaskDef> clozeSubtaskdefs;
    // @CollectionOfElements(targetElement = TextSubTaskDef.class)
    // private final List<TextSubTaskDef> textSubtaskdefs;
    // @CollectionOfElements(targetElement = PaintSubTaskDef.class)
    // private final List<PaintSubTaskDef> paintSubtaskdefs;
    // @CollectionOfElements(targetElement = MappingSubTaskDef.class)
    // private final List<MappingSubTaskDef> mappingSubtaskdefs;

    public BasicUser() {
        // addonSubtaskdefs = new ArrayList<AddonSubTaskDef>();
        // mcSubtaskdefs = new ArrayList<McSubTaskDef>();
        // clozeSubtaskdefs = new ArrayList<ClozeSubTaskDef>();
        // textSubtaskdefs = new ArrayList<TextSubTaskDef>();
        // paintSubtaskdefs = new ArrayList<PaintSubTaskDef>();
        // mappingSubtaskdefs = new ArrayList<MappingSubTaskDef>();
        taskdefs = new ArrayList<ComplexTaskDef>();
        subtaskdefs = new ArrayList<SubTaskDefType>();

        roles = new Roles(Roles.USER);
        password = new BasicPassword();
    }

    /**
     * @return the subtaskdefs
     */
    public List<SubTaskDefType> getSubtaskdefs() {
        return subtaskdefs;
    }

    /**
     * Filter {@link #getSubtaskdefs()} to instances of a specific {@link SubTaskDefType}. CAUTION: You must not add new
     * instances to this list, they won't get persisted. Use {@link #getSubtaskdefs()}.add(...) instead.
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    public <T extends SubTaskDefType> Collection<T> getSubtaskdefsOf(final Class<T> clazz) {
        final Set<T> res = new HashSet<T>();
        for (final SubTaskDefType st : getSubtaskdefs()) {
            if (st.getClass().isAssignableFrom(clazz)) {
                res.add((T) st);
            }
        }
        return res;
    }

    public void addRole(final String role) {
        this.roles.add(role);
    }

    // public List<AddonSubTaskDef> getAddonSubtaskdefs() {
    // return addonSubtaskdefs;
    // }

    // public List<ClozeSubTaskDef> getClozeSubtaskdefs() {
    // return clozeSubtaskdefs;
    // }

    public Integer getId() {
        return id;
    }

    // public List<MappingSubTaskDef> getMappingSubtaskdefs() {
    // return mappingSubtaskdefs;
    // }
    //
    // public List<McSubTaskDef> getMcSubtaskdefs() {
    // return mcSubtaskdefs;
    // }
    //
    // public List<PaintSubTaskDef> getPaintSubtaskdefs() {
    // return paintSubtaskdefs;
    // }

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

    public List<ComplexTaskDef> getTaskdefs() {
        return taskdefs;
    }

    // public List<TextSubTaskDef> getTextSubtaskdefs() {
    // return textSubtaskdefs;
    // }

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
    public boolean hasRole(final String role) {
        return roles.contains(role);
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setPassword(final BasicPassword password) {
        this.password = password;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

}
