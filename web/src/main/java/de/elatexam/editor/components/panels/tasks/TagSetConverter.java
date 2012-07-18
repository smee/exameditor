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
package de.elatexam.editor.components.panels.tasks;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.util.convert.IConverter;

/**
 * @author Steffen Dienst
 *
 */
public class TagSetConverter implements IConverter<Set<String>> {
  @Override
  public Set<String> convertToObject(String value, Locale locale) {
    Set<String> res = new HashSet<String>();
    for(String s:value.split(",")){
      res.add(s.trim());
    }
    return res;
  }

  @Override
  public String convertToString(Set<String> tags, Locale locale) {
    StringBuilder sb = new StringBuilder();
    for (String tag : tags) {
      sb.append(tag).append(",");
    }
    if(sb.length()>0)
      sb.deleteCharAt(sb.length()-1);
    return sb.toString();
  }
}