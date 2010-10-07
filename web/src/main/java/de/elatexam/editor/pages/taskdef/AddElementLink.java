/*

Copyright (C) 2010 Steffen Dienst

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
package de.elatexam.editor.pages.taskdef;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;

import com.google.common.collect.ImmutableMap;

import de.elatexam.editor.pages.TaskDefPage;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.util.Stuff;
import de.elatexam.model.Category;
import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.ClozeTaskBlock.ClozeConfig;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.ComplexTaskDef.Config;
import de.elatexam.model.ComplexTaskDef.Config.CorrectionMode;
import de.elatexam.model.ComplexTaskDef.Config.CorrectionMode.Regular;
import de.elatexam.model.MappingTaskBlock;
import de.elatexam.model.MappingTaskBlock.MappingConfig;
import de.elatexam.model.McTaskBlock;
import de.elatexam.model.McTaskBlock.McConfig;
import de.elatexam.model.PaintTaskBlock;
import de.elatexam.model.TaskBlock;
import de.elatexam.model.TaskblockConfig;
import de.elatexam.model.TextTaskBlock;

/**
 * @author Steffen Dienst
 *
 */
public class AddElementLink<T> extends AjaxLink<T> {
    final static ImmutableMap<Class<?>, Integer> childMap = new ImmutableMap.Builder<Class<?>, Integer>()

    .put(BasicUser.class, 0)
    .put(ComplexTaskDef.class, 1)
    .put(Category.class, 2)
    .put(McTaskBlock.class, 3)
    .put(MappingTaskBlock.class, 4)
    .put(ClozeTaskBlock.class, 5)
    .put(TextTaskBlock.class, 6)
    .put(PaintTaskBlock.class, 7)

    .build();

    private TaskDefPage taskDefPage;

    private TaskBlockSelectorModalWindow selectTaskBlockModal;

    public AddElementLink(String id, TaskBlockSelectorModalWindow selectTaskBlockModal, TaskDefPage taskDefPage) {
        super(id);
        this.taskDefPage = taskDefPage;
        this.selectTaskBlockModal = selectTaskBlockModal;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {

        Object selectedObject = taskDefPage.getTreeSelection().getObject();
        List<Object> toSave = new LinkedList<Object>();
        switch (childMap.get(selectedObject.getClass())) {
        case 0: // create a new complextaskdef
            ComplexTaskDef newtaskdef = new ComplexTaskDef();
            newtaskdef.setTitle("?????");
            Config config = new Config();
            config.setCorrectionMode(new CorrectionMode());
            config.getCorrectionMode().setRegular(new Regular());
            newtaskdef.setConfig(config);
            ((BasicUser) selectedObject).getTaskdefs().add(newtaskdef);
            toSave.add(newtaskdef);
            target.addComponent(taskDefPage.getTree());
            break;
        case 1: //create a new category
            Category cat = new Category();
            cat.setTitle("????");
            // cat.setId(""); // TODO set category id
            ((ComplexTaskDef) selectedObject).getCategory().add(cat);
            toSave.add(cat);
            target.addComponent(taskDefPage.getTree());
            break;
        case 2: // show taskblock selection modal window
            selectTaskBlockModal.show(target);
            break;
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
            // TODO select an existing taskdef
            break;
        default:
            break;
        }
        Stuff.saveAll(selectedObject);
    }

    /**
     * Create a new instance of the given taskblock type. Configure child objects.
     *
     * @param taskblockclass
     */
    public void createTaskblock(Class<? extends TaskBlock> taskblockclass) {
        Object selectedObject = taskDefPage.getTreeSelection().getObject();
        if (selectedObject instanceof Category) {
            try {
                TaskBlock taskblock = taskblockclass.newInstance();
                taskblock.setConfig(new TaskblockConfig());

                ((Category) selectedObject).getTaskBlocks().add(taskblock);

                // set subclass specific config
                switch (childMap.get(taskblockclass)) {
                case 3:
                    ((McTaskBlock) taskblock).setMcConfig(new McConfig(new McConfig.Regular(), null));
                    break;
                case 4:
                    ((MappingTaskBlock) taskblock).setMappingConfig(new MappingConfig());
                    break;
                case 5:
                    ((ClozeTaskBlock) taskblock).setClozeConfig(new ClozeConfig());
                    break;
                default:
                    break;
                }

                Stuff.saveAll(selectedObject);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

}
