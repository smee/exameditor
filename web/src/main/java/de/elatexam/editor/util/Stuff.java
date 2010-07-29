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
package de.elatexam.editor.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jvnet.hyperjaxb3.item.Item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import de.elatexam.model.AddonSubTaskDef;
import de.elatexam.model.Category;
import de.elatexam.model.Category.CategoryTaskBlocksItem;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.MappingTaskBlock;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.McTaskBlock;
import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.PaintTaskBlock;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TaskBlock;
import de.elatexam.model.TextSubTaskDef;
import de.elatexam.model.TextTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.ComplexTaskDefType.CategoryType.AddonTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.TaskBlockType;

/**
 * Misc. helper functions.
 *
 * @author Steffen Dienst
 *
 */
public class Stuff {
    public static Map<Class<?>, String> subtaskNames = new ImmutableMap.Builder<Class<?>, String>()
            .put(McSubTaskDef.class, "Mc")
            .put(McTaskBlock.class, "Mc")

            .put(AddonSubTaskDef.class, "Addon")
            .put(AddonTaskBlock.class, "Addon")

            .put(ClozeSubTaskDef.class, "Cloze")
            .put(ClozeTaskBlock.class, "Cloze")

            .put(TextSubTaskDef.class, "Text")
            .put(TextTaskBlock.class, "Text")

            .put(MappingSubTaskDef.class, "Mapping")
            .put(MappingTaskBlock.class, "Mapping")

            .put(PaintSubTaskDef.class, "Paint")
            .put(PaintTaskBlock.class, "Paint")
            .build();

  /**
   * Call a no-parameter method, return it's value. Rethrow whatever
   * {@link Class#getMethod(String, Class...)} or
   * {@link Method#invoke(Object, Object...)} throws.
   *
   * @param o
   * @param methodName
   * @return
   * @throws Exception
   */
  public static Object call(final Object o, final String methodName) throws Exception {
    final Method method = o.getClass().getMethod(methodName);
    return method.invoke(o);
  }

  /**
   * Invoke similary named methods involving {@link SubTaskDef}s.
   *
   * @param o
   *          the object the method should be invoked on
   * @param methodNameTemplate
   *          string including %s for the subtaskdef specific name
   * @param clazz
   * @return
   * @throws Exception
   */
    public static Object call(final Object o, final String methodNameTemplate, final Class<?> clazz, Object... args) throws Exception {
    final Method method = o.getClass().getMethod(String.format(methodNameTemplate, subtaskNames.get(clazz)));
    return method.invoke(o,args);
  }

  /**
   * Get the items (subtaskdef|choice(subtaskdef)+)* of a taskblock. Needs to be done via reflection, because the items
   * have no common interface but similar method names. This method assumes that each subclass of {@link TaskBlockType}
   * has exactly one method named "get...SubTaskDefOrChoiceItems" that gets invoked.
   *
   * @param tb
   *          taskblock
   * @return list of items or empty list.
   */
    public static List<? extends Item> getItems(final TaskBlock tb) {
    for (final Method m : tb.getClass().getMethods()) {
      final String name = m.getName();
      if (name.startsWith("get") && name.endsWith("SubTaskDefOrChoiceItems")) {
        try {
          return (List<? extends Item>) m.invoke(tb);
        } catch (final IllegalArgumentException e) {
          e.printStackTrace();
        } catch (final IllegalAccessException e) {
          e.printStackTrace();
        } catch (final InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
    return Lists.newArrayList();
  }

  public static <T extends SubTaskDef> Collection<T> getAllSubtaskdefs(final ComplexTaskDef taskdef, final Class<T> clazz)
  throws Exception {
    final Collection<T> stds = new ArrayList<T>();
    for (final Category cat : taskdef.getCategory()) {
      for (final CategoryTaskBlocksItem block : cat.getTaskBlocksItems()) {
                stds.addAll(getAllSubtaskdefsFromBlock((TaskBlock) call(block, "getItem%sTaskBlock", clazz), clazz));
      }
    }
    return stds;
  }

    public static <T extends SubTaskDef> Collection<T> getAllSubtaskdefsFromBlock(final TaskBlock block, final Class<T> clazz)
  throws Exception {
    final Collection<T> stds = new ArrayList<T>();
    if (block == null)
        return stds;
    final List items = (List) call(block, "get%sSubTaskDefOrChoiceItems", clazz);
    if (items != null) {
      for (final Object item : items) {
        final T st = (T) call(item, "getItem%sSubTaskDef", clazz);
        if (st != null) {
          stds.add(st);
        } else {
          final Object choice = call(item, "getItemChoice");
          stds.addAll((Collection<T>) call(choice, "get%sSubTaskDef", clazz));
        }
      }
    }
    return stds;
  }

}
