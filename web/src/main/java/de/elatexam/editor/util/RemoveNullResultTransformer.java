package de.elatexam.editor.util;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.transform.BasicTransformerAdapter;

/**
 * Workaround for hibernate queries, that when a query should not return any results at all, instead returns a list with
 * null values.
 * 
 * @author Steffen Dienst
 * 
 */
public class RemoveNullResultTransformer extends BasicTransformerAdapter {
  public static final RemoveNullResultTransformer INSTANCE = new RemoveNullResultTransformer();

  private RemoveNullResultTransformer(){}
  @Override
  public List transformList(final List list) {
    final List result = new ArrayList(list.size());
    for (final Object entry : list) {
      final Object[] arr = (Object[]) entry;
      if (entry != null && arr[0] != null) {
        result.add(arr[0]);
      }
    }
    return result;
  }
}
