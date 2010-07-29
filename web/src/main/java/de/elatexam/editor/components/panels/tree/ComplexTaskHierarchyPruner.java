package de.elatexam.editor.components.panels.tree;

import java.util.Iterator;
import java.util.List;

import org.jvnet.hyperjaxb3.item.Item;

import wickettree.ITreeProvider;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.util.Stuff;
import de.elatexam.model.Category;
import de.elatexam.model.Category.CategoryTaskBlocksItem;
import de.elatexam.model.ComplexTaskDef;
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
    private Object findParentOf(final Object child) {
        final Iterator<?> it = treeProvider.getRoots();
        while (it.hasNext()) {
            final Object root = it.next();
            final Object parent = findParentOf(root, child);
            if (parent != null)
                return parent;
        }
        return null;
    }

    private Object clearPhysicalParent(final Object child, final Object logicalParent) {
        if (child instanceof TaskBlock) {
            final Category cat = (Category) logicalParent;
            for (final CategoryTaskBlocksItem tbi : cat.getTaskBlocksItems()) {
                if (tbi.getItem() == child) {
                    cat.getTaskBlocksItems().remove(tbi);
                    // tbi.setItem(null);
                    // TODO unlink subtaskdefs or better: do not propagate delete to subtaskdefs.... needs HJ3 fix, see
                    // http://jira.highsource.org/browse/HJIII-26
                    return tbi;
                }
            }
        } else if (child instanceof SubTaskDef) {
            final TaskBlock tb = (TaskBlock) logicalParent;
            try {
                final List<Item<?>> itemsList = (List) Stuff.call(tb, "get%sSubTaskDefOrChoiceItems", (Class<? extends de.elatexam.model.SubTaskDef>) child.getClass());
                for (final Item<?> item : itemsList) {
                    if (item.getItem() == child) {
                        itemsList.remove(item);
                        return item;
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
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
                    ((Long) Stuff.call(potentialParent, "getHjid")).equals(Stuff.call(child, "getHjid"));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
