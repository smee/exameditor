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
package de.elatexam.editor;

import net.databinder.auth.AuthDataSessionBase;
import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.Request;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;

import de.elatexam.editor.user.BasicUser;

/**
 * 
 * @author Steffen Dienst
 * 
 */
public class TaskEditorSession extends AuthDataSessionBase<BasicUser> {
  /**
   * 
   */
  TaskEditorSession(final Request request) {
    super(request);
  }

  public static TaskEditorSession get() {
    return (TaskEditorSession) WebSession.get();
  }


  /*
   * (non-Javadoc)
   * 
   * @see net.databinder.auth.AuthDataSessionBase#createUserModel(net.databinder.auth.data.DataUser)
   */
  @Override
  public IModel<BasicUser> createUserModel(final BasicUser user) {
    return new HibernateObjectModel<BasicUser>(user);
  }
}