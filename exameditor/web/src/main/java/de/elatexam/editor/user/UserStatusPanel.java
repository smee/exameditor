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
package de.elatexam.editor.user;

import net.databinder.auth.components.hib.DataUserStatusPanel;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

import de.elatexam.editor.pages.PatchedAdminPage;

/**
 * @author Steffen Dienst
 * 
 */
public class UserStatusPanel extends DataUserStatusPanel {
  public UserStatusPanel(final String id) {
    super(id);
  }

  @Override
  protected Class<? extends WebPage> adminPageClass() {
    return PatchedAdminPage.class;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.databinder.auth.components.DataUserStatusPanelBase#getSignInLink(java.lang.String)
   */
  @Override
  protected Link getSignInLink(final String id) {
    // return non working invisible link
    return new Link(id) {
      @Override
      public boolean isVisible() {
        return false;
      }

      @Override
      public void onClick() {
      }
    };
  }
}