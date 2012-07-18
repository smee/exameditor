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

import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.codesmell.wicket.tagcloud.TagCloudData;
import org.codesmell.wicket.tagcloud.TagData;

import de.elatexam.editor.TaskEditorSession;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.util.Stuff;

/**
 * @author Steffen Dienst
 *
 */
public class SubtaskdefTagModel extends AbstractReadOnlyModel<TagCloudData> {
  private Set<String> selectedTags = new HashSet<String>();
  public SubtaskdefTagModel(){
    
  }

  /* (non-Javadoc)
   * @see org.apache.wicket.model.IModel#getObject()
   */
  @Override
  public TagCloudData getObject() {
    BasicUser currentUser = TaskEditorSession.get().getUser();
    
    return new TagCloudData(Stuff.getTagData(currentUser, selectedTags)){
      
      @Override
      protected AbstractLink getLink(String id, final TagData tagdata) {
        return new AjaxFallbackLink(id) {

          @Override
          public void onClick(AjaxRequestTarget target) {
            selectedTags.add(tagdata.getName());
            send(getPage(), Broadcast.BREADTH, new TagSelectedEvent(target));
          }
        };
      }
    };
  }
}
