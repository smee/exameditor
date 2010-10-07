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
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.McTaskBlock;
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
public class ComplexTaskHierarchyPruner {
    private ITreeProvider<Object> treeProvider;

    /**
     * Default constructor.
     *
     * @param treeProvider
     */
    public ComplexTaskHierarchyPruner(ITreeProvider<Object> treeProvider) {
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
     * @param element
     * @param to
     * @param anchor
     */
    public void moveElement(Object element, Object to, Anchor anchor) {
        if (to instanceof McTaskBlock) {
            McTaskBlock toTaskblock = (McTaskBlock) to;
            Object parent = findParentOf(element);
            if (parent != toTaskblock) {
                // remove from current taskblock
                ((McTaskBlock) parent).getMcSubTaskDef().remove(element);
                toTaskblock.getMcSubTaskDef().add((McSubTaskDef) element);

                Stuff.saveAll(parent, toTaskblock);
            }
        } else if (to instanceof McSubTaskDef) {
            // change order
            McTaskBlock toTaskblock = (McTaskBlock) findParentOf(to);

            McTaskBlock fromTaskblock = (McTaskBlock) findParentOf(element);
            fromTaskblock.getMcSubTaskDef().remove(element);

            List<McSubTaskDef> subtaskdefs = toTaskblock.getMcSubTaskDef();
            int indexOfTo = subtaskdefs.indexOf(to);
            if (anchor == Anchor.BOTTOM) {
                indexOfTo++;
            }
            subtaskdefs.add(indexOfTo, (McSubTaskDef) element);

            // manifest order by using a hack:
            int idx = 0;
            for (McSubTaskDef std : subtaskdefs) {
                std.setXmlid(SortableIdModel.getTaggedId(std.getXmlid(), idx));
                idx++;
            }
            Stuff.saveAll(fromTaskblock, toTaskblock);
        }
    }

    private Object clearPhysicalParent(final Object child, final Object logicalParent) {
        if (child instanceof TaskBlock) {
            final Category cat = (Category) logicalParent;
            for (final TaskBlock tbi : cat.getTaskBlocks()) {
                if (tbi == child) {
                    cat.getTaskBlocks().remove(tbi);
                    // tbi.setItem(null);
                    // TODO unlink subtaskdefs or better: do not propagate delete to subtaskdefs.... needs HJ3 fix, see
                    // http://jira.highsource.org/browse/HJIII-26
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
