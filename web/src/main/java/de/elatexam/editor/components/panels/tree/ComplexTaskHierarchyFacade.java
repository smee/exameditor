package de.elatexam.editor.components.panels.tree;

import java.util.Iterator;
import java.util.List;

import wicketdnd.Anchor;
import wickettree.ITreeProvider;
import de.elatexam.editor.components.panels.tasks.SortableIdModel;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.util.Stuff;
import de.elatexam.model.Category;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.Indexed;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TaskBlock;

/**
 * This class knows about the concrete class hierarchy of the generated complextaskdef domain model. It allows to find
 * direct references between parents and children and knows how to remove these references. This allows deleting the
 * child objects without throwing up hibernate.
 *
 * @author Steffen Dienst
 *
 */
public class ComplexTaskHierarchyFacade {
  private ITreeProvider<Object> treeProvider;

    /**
     * Default constructor.
     *
     * @param treeProvider
     */
  public ComplexTaskHierarchyFacade(ITreeProvider<Object> treeProvider) {
        this.treeProvider = treeProvider;
    }

    /**
     * Removes object from it's parent. Returns that object, that needs to get deleted from the database.
     *
     * @param child
     * @return the object to delete
     */
    public Object removeFromParent(final Object child) {
        final Object parent = findParentOf(child);
        if (parent != null) {
            if (parent instanceof BasicUser) {
                ((BasicUser) parent).getTaskdefs().remove(child);
            } else if (parent instanceof ComplexTaskDef) {
                ((ComplexTaskDef) parent).getCategory().remove(child);
            } else
                return clearPhysicalParent(child, parent);
        }
        return child;
    }

    /**
     * Find the model where is true: this.getChildren(parent).contains(child).
     *
     * @param child
     * @return
     */
    public Object findParentOf(final Object child) {
        final Iterator<?> it = treeProvider.getRoots();
        while (it.hasNext()) {
            final Object root = it.next();
            final Object parent = findParentOf(root, child);
            if (parent != null)
                return parent;
        }
        return null;
    }

    /**
     * Move element from original parent to the new one depending on the types of domain objects involved.
     * @param element
     * @param to
     * @param anchor
     * @return true if a move occured, false if not
     */
    public boolean moveElement(Object element, Object to, Anchor anchor) {
        if (element == to)
            return false;
        if (to instanceof TaskBlock) {
        	if(element instanceof SubTaskDef)
        		return move((SubTaskDef)element, (TaskBlock)to,anchor,true);
        	else if(element instanceof TaskBlock){
        		return move((TaskBlock)element,(TaskBlock)to,anchor,true);
        	}
        } else if (to instanceof SubTaskDef) {
            return move((SubTaskDef)element,(SubTaskDef)to,anchor);
        }else if (to instanceof Category){
        	return move((TaskBlock)element,(Category)to,anchor,true);
        }
        return false;
    }
	/**
	 * Copy element from original parent to the new one depending on the types of domain objects involved.
	 * @param droppedObject
	 * @param droppedOn
	 * @param anchor
	 * @return
	 */
	public boolean copyElement(Object droppedObject, Object droppedOn,
			Anchor anchor) {
		if( droppedObject instanceof TaskBlock && droppedOn instanceof Category){
			// FIXME create a copy of the taskblock, doesn't work this way
			return move((TaskBlock)droppedObject,(Category)droppedOn,anchor,false);
		}else if(droppedObject instanceof SubTaskDef){
			if(droppedOn instanceof TaskBlock){
				return move((SubTaskDef)droppedObject, (TaskBlock)droppedOn, anchor,false);
			}
		}
		return false;
	}

	private boolean move(TaskBlock tb, TaskBlock to, Anchor anchor, boolean isMove) {
		Category fromC = (Category) findParentOf(tb);
		Category toC = (Category) findParentOf(to);
		if(fromC!=toC){
			return move(tb,toC,anchor,isMove);
		}else{
			//TODO how can the order of taskblocks be changed? JPA does not keep list semantics (uses persistent bag....)
		}
		return false;
	}

	private boolean move(TaskBlock element, Category to, Anchor anchor, boolean isMove) {
    	Category fromC = (Category) findParentOf(element);
    	TaskBlock b = (TaskBlock) element;
    	if(isMove && fromC!=null)
    		fromC.getTaskBlocks().remove(b);
    	to.getTaskBlocks().add(b);
    	Stuff.saveAll(fromC,to);
    	return true;
	}

	private boolean move(SubTaskDef element, SubTaskDef to, Anchor anchor) {
		// change order
        TaskBlock toTaskblock = (TaskBlock) findParentOf(to);

        TaskBlock fromTaskblock = (TaskBlock) findParentOf(element);
        if(fromTaskblock!=null)
        	Stuff.getSubtaskDefs(fromTaskblock).remove(element);

        List<SubTaskDef> subtaskdefs = Stuff.getSubtaskDefs(toTaskblock);
        int indexOfTo = subtaskdefs.indexOf(to);
        if (anchor == Anchor.BOTTOM) {
            indexOfTo++;
        }
        subtaskdefs.add(indexOfTo, (SubTaskDef) element);

        // manifest order by using a hack:
        int idx = 0;
        for (SubTaskDef std : subtaskdefs) {
            std.setXmlid(SortableIdModel.getTaggedId(std.getXmlid(), idx));
            idx++;
        }
        Stuff.saveAll(fromTaskblock, toTaskblock);
        return true;
	}

	private boolean move(SubTaskDef element, TaskBlock to, Anchor anchor, boolean isMove) {
        Object parent = findParentOf(element);
        if (parent != to) {
            // remove from current taskblock
        	if(isMove && parent != null)
        		removeFromParent(element);
            Stuff.getSubtaskDefs(to).add((SubTaskDef) element);

            Stuff.saveAll(parent, to);
            return true;
        }
        return false;
	}

	private Object clearPhysicalParent(final Object child, final Object logicalParent) {
        if (child instanceof TaskBlock) {
            final Category cat = (Category) logicalParent;
            for (final TaskBlock tbi : cat.getTaskBlocks()) {
                if (tbi == child) {
                    cat.getTaskBlocks().remove(tbi);
                    return tbi;
                }
            }
        } else if (child instanceof SubTaskDef) {
            final TaskBlock tb = (TaskBlock) logicalParent;
            try {
                final List<SubTaskDef> itemsList = (List) Stuff.call(tb, "get%sSubTaskDef", child.getClass());
                for (final SubTaskDef subtaskdef : itemsList) {
                    if (subtaskdef == child) {
                        itemsList.remove(subtaskdef);
                        return subtaskdef;
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        // all other elements have a single child, stupid jaxb default code...
        return logicalParent;
    }

    private Object findParentOf(final Object current, final Object child) {
        final Iterator<?> it = treeProvider.getChildren(current);
        while (it.hasNext()) {
            final Object potentialParent = it.next();
            if (isSamePersistedObject(potentialParent, child))
                return current;
            final Object parent = findParentOf(potentialParent, child);
            if (parent != null)
                return parent;
        }
        return null;
    }

    /**
     * Compare the generated primary keys instead of using the semantical equals method.
     *
     * @param potentialParent
     * @param child
     * @return
     */
    private boolean isSamePersistedObject(Object potentialParent, Object child) {
        try {
            return
            // same class?
            potentialParent.getClass().equals(child.getClass())
                    &&
                    // same primary key?
                    ((Indexed) potentialParent).getHjid().equals(((Indexed) child).getHjid());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
