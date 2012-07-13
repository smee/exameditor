package de.elatexam.editor.components.panels.tree;

import java.util.Iterator;
import java.util.List;

import wicketdnd.Anchor;
import wickettree.ITreeProvider;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.util.Stuff;
import de.elatexam.model.Category;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.Indexed;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.manual.HomogeneousTaskBlock;

/**
 * This class knows about the concrete class hierarchy of the generated complextaskdef domain model. It allows to find
 * direct references between parents and children and knows how to remove these references. This allows deleting the
 * child objects without throwing up hibernate.
 *
 * @author Steffen Dienst
 *
 */
public class ComplexTaskHierarchyFacade {
  private ITreeProvider<Indexed> treeProvider;

    /**
     * Default constructor.
     *
     * @param treeProvider
     */
  public ComplexTaskHierarchyFacade(ITreeProvider<Indexed> treeProvider) {
        this.treeProvider = treeProvider;
    }

    /**
     * Removes object from it's parent. Returns that object, that needs to get deleted from the database.
     *
     * @param child
     * @return the object to delete
     */
    public Indexed removeFromParent(final Indexed child) {
        final Indexed parent = findParentOf(child);
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
    public Indexed findParentOf(final Indexed child) {
        final Iterator<? extends Indexed> it = treeProvider.getRoots();
        while (it.hasNext()) {
            final Indexed root = it.next();
            final Indexed parent = findParentOf(root, child);
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
        if (to instanceof HomogeneousTaskBlock) {
        	if(element instanceof SubTaskDef)
        		return move((SubTaskDef)element, (HomogeneousTaskBlock)to,anchor,true);
        	else if(element instanceof HomogeneousTaskBlock){
        		return move((HomogeneousTaskBlock)element,(HomogeneousTaskBlock)to,anchor,true);
        	}
        } else if (to instanceof SubTaskDef) {
            return move((SubTaskDef)element,(SubTaskDef)to,anchor);
        }else if (to instanceof Category){
        	return move((HomogeneousTaskBlock)element,(Category)to,anchor,true);
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
		if( droppedObject instanceof HomogeneousTaskBlock && droppedOn instanceof Category){
			// FIXME create a copy of the taskblock, doesn't work this way
			return move((HomogeneousTaskBlock)droppedObject,(Category)droppedOn,anchor,false);
		}else if(droppedObject instanceof SubTaskDef){
			if(droppedOn instanceof HomogeneousTaskBlock){
				return move((SubTaskDef)droppedObject, (HomogeneousTaskBlock)droppedOn, anchor,false);
			}
		}
		return false;
	}

	private boolean move(HomogeneousTaskBlock tb, HomogeneousTaskBlock to, Anchor anchor, boolean isMove) {
		Category fromC = (Category) findParentOf(tb);
		Category toC = (Category) findParentOf(to);
		if(fromC!=toC){
			return move(tb,toC,anchor,isMove);
		}else{
			//TODO how can the order of taskblocks be changed? JPA does not keep list semantics (uses persistent bag....)
		}
		return false;
	}

	private boolean move(HomogeneousTaskBlock element, Category to, Anchor anchor, boolean isMove) {
    	Category fromC = (Category) findParentOf(element);
    	HomogeneousTaskBlock b = (HomogeneousTaskBlock) element;
    	if(isMove && fromC!=null)
    		fromC.getTaskBlocks().remove(b);
    	to.getTaskBlocks().add(b);
    	Stuff.saveAll(fromC,to);
    	return true;
	}

	private boolean move(SubTaskDef element, SubTaskDef target, Anchor anchor) {
		// change order
        HomogeneousTaskBlock targetTaskblock = (HomogeneousTaskBlock) findParentOf(target);

        HomogeneousTaskBlock sourceTaskblock = (HomogeneousTaskBlock) findParentOf(element);
        if(sourceTaskblock!=null)
        	Stuff.getSubtaskDefs(sourceTaskblock).remove(element);

        List<SubTaskDef> subtaskdefs = Stuff.getSubtaskDefs(targetTaskblock);
        int indexOfTo = subtaskdefs.indexOf(target);
        if (anchor == Anchor.BOTTOM) {
            indexOfTo++;
        }
        subtaskdefs.add(indexOfTo,  element);

        Stuff.saveAll(sourceTaskblock, targetTaskblock);
        return true;
	}

	private boolean move(SubTaskDef element, HomogeneousTaskBlock to, Anchor anchor, boolean isMove) {
        Indexed parent = findParentOf(element);
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

	private Indexed clearPhysicalParent(final Indexed child, final Indexed logicalParent) {
        if (child instanceof HomogeneousTaskBlock) {
            final Category cat = (Category) logicalParent;
            for (final HomogeneousTaskBlock tbi : cat.getTaskBlocks()) {
                if (tbi == child) {
                    cat.getTaskBlocks().remove(tbi);
                    return tbi;
                }
            }
        } else if (child instanceof SubTaskDef) {
            final HomogeneousTaskBlock tb = (HomogeneousTaskBlock) logicalParent;
            try {
                final List<? extends SubTaskDef> itemsList = tb.getSubtaskDefs();
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

    private Indexed findParentOf(final Indexed current, final Indexed child) {
        final Iterator<? extends Indexed> it = treeProvider.getChildren(current);
        while (it.hasNext()) {
            final Indexed potentialParent = it.next();
            if (isSamePersistedObject(potentialParent, child))
                return current;
            final Indexed parent = findParentOf(potentialParent, child);
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
    private boolean isSamePersistedObject(Indexed potentialParent, Indexed child) {
        try {
            return
            // same class?
            potentialParent.getClass().equals(child.getClass())
                    &&
                    // same primary key?
                    potentialParent.getHjid().equals(child.getHjid());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
