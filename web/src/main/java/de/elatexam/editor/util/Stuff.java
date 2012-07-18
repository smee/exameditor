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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import net.databinder.hib.Databinder;

import org.codesmell.wicket.tagcloud.TagData;
import org.hibernate.Query;
import org.hibernate.Session;

import de.elatexam.editor.TaskEditorSession;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.model.Category;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TaskBlock;
import de.elatexam.model.manual.HomogeneousTaskBlock;
import de.thorstenberger.taskmodel.complex.jaxb.TaskBlockType;

/**
 * Misc. helper functions.
 *
 * @author Steffen Dienst
 *
 */
public class Stuff {
	static JAXBContext context;
	static{
		try {
			context=JAXBContext.newInstance(ComplexTaskDef.class);
		} catch (JAXBException e) {
			e.printStackTrace();
			context = null;
		}
	}
	public static JAXBContext getContext(){
		return context;
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
    public static List<SubTaskDef> getSubtaskDefs(final HomogeneousTaskBlock tb) {
    	return (List<SubTaskDef>) tb.getSubtaskDefs();
  }

    public static <T extends SubTaskDef> Collection<T> getAllSubtaskdefs(final ComplexTaskDef taskdef) {
    final Collection<T> stds = new ArrayList<T>();
    for (final Category cat : taskdef.getCategory()) {
            for (final TaskBlock block : cat.getTaskBlocks()) {
                stds.addAll((Collection<? extends T>) getSubtaskDefs((HomogeneousTaskBlock) block));
      }
    }
    return stds;
  }
    /**
     * Make all xmlids of every subtaskdef unique by prepending an increasing number to it.
     * @param taskdef
     */
    public static void makeIDsUnique(ComplexTaskDef taskdef){
    	int i=0;
    	for(SubTaskDef std: getAllSubtaskdefs(taskdef)){
    		std.setXmlid(i++ + std.getXmlid());
    	}
    }

    /**
     * TODO create DAO layer!
     *
     * @param objects
     */
    public static void saveAll(Object... objects) {
        final Session session = Databinder.getHibernateSession();
        for (Object obj : objects) {
          if (obj != null && !session.contains(obj)) {
            session.saveOrUpdate(obj);
          }
        }
        session.getTransaction().commit();
    }

    public static <T> T newInstance(Class<T> clazz){
      try {
        return clazz.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
        return null;
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        return null;
      }
    }
    /**
     * @return
     */
    public static List<String> getAllUniqueTags() {
      return Databinder.getHibernateSession().createQuery("select distinct elements(s.tags) from de.elatexam.model.manual.TaggedSubtaskdef s").list();
    }
    
    /**
     * Return data for a tag cloud for the given user.
     * @param user
     * @return
     */
    public static List<TagData> getTagData(BasicUser user, Collection<String> includedTags){
      StringBuilder queryString = new StringBuilder();
      queryString.append(String.format("select elements(s.tags) from de.elatexam.model.manual.TaggedSubtaskdef s, de.elatexam.editor.user.BasicUser u where u.username='%s'", TaskEditorSession.get().getUser().getUsername()));
      for(String inclTag: includedTags)
        queryString.append(" and '").append(inclTag).append("' in elements(s.tags)");
        
      Query query = Databinder.getHibernateSession().createQuery(queryString.toString());
      Map<String, Integer> tagCounts = new HashMap<String,Integer>();
      List<String> queryResult = query.list();
      for(String tag: queryResult){
        if(!tagCounts.containsKey(tag)){
          tagCounts.put(tag, 0);
        }else{
          tagCounts.put(tag, tagCounts.get(tag) + 1);
        }
      }        
      List<TagData> res = new ArrayList<TagData>();
      for(String tag: tagCounts.keySet())
        res.add(new TagData(tag, tagCounts.get(tag)));
      
      return res;
    }
}
