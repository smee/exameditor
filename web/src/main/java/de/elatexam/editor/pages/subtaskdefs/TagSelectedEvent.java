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
package de.elatexam.editor.pages.subtaskdefs;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author Steffen Dienst
 *
 */
public class TagSelectedEvent {

  private AjaxRequestTarget target;

  /**
   * @param target
   */
  public TagSelectedEvent(AjaxRequestTarget target) {
    this.target = target;
  }

  /**
   * @return the target
   */
  public AjaxRequestTarget getTarget() {
    return target;
  }

}
