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
package de.elatexam.model.manual;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import de.elatexam.model.SubTaskDef;

/**
 * @author Steffen Dienst
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public abstract class TaggedSubtaskdef extends SubTaskDef {
  @XmlTransient
  private Set<String> tags;
  /**
   * 
   */
  public TaggedSubtaskdef() {
  }

  /**
   * @param problem
   * @param hint
   * @param correctionHint
   * @param xmlid
   * @param interactiveFeedback
   * @param trash
   * @param inputLanguage
   */
  public TaggedSubtaskdef(String problem, String hint, String correctionHint, String xmlid, Boolean interactiveFeedback,
      Boolean trash, String inputLanguage) {
    super(problem, hint, correctionHint, xmlid, interactiveFeedback, trash, inputLanguage);
  }

  @XmlTransient
  @ElementCollection
  public Set<String> getTags(){
    if(tags == null)
      tags = new HashSet<String>();
    return tags;
  }

  /**
   * @param tags the tags to set
   */
  public void setTags(Set<String> tags) {
    this.tags = tags;
  }
}
