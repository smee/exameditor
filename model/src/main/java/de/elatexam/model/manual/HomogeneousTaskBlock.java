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

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import de.elatexam.model.AddonTaskBlock;
import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.MappingTaskBlock;
import de.elatexam.model.McTaskBlock;
import de.elatexam.model.PaintTaskBlock;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TaskBlock;
import de.elatexam.model.TaskblockConfig;
import de.elatexam.model.TextTaskBlock;

/**
 * @author Steffen Dienst
 *
 */
@XmlTransient
public abstract class HomogeneousTaskBlock extends TaskBlock {

  /**
   * 
   */
  public HomogeneousTaskBlock() {
    super();
  }

  /**
   * @param config
   */
  public HomogeneousTaskBlock(TaskblockConfig config) {
    super(config);
  }
  
  /**
   * Common accessor to subtaskdefs.
   * @return
   */
  public List<? extends SubTaskDef> getSubtaskDefs(){
    if(McTaskBlock.class.isInstance(this))
      return ((McTaskBlock)(Object)this).getMcSubTaskDef();
    if(MappingTaskBlock.class.isInstance(this))
      return ((MappingTaskBlock)(Object)this).getMappingSubTaskDef();
    if(ClozeTaskBlock.class.isInstance(this))
      return ((ClozeTaskBlock)(Object)this).getClozeSubTaskDef();
    if(TextTaskBlock.class.isInstance(this))
      return ((TextTaskBlock)(Object)this).getTextSubTaskDef();
    if(PaintTaskBlock.class.isInstance(this))
      return ((PaintTaskBlock)(Object)this).getPaintSubTaskDef();
    if(AddonTaskBlock.class.isInstance(this))
      return ((AddonTaskBlock)(Object)this).getAddonSubTaskDef();
          
    return Collections.emptyList();
  }

}
