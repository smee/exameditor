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
package de.elatexam.editor.pages;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.wicketstuff.tagit.TagItTextField;

/**
 * @author Steffen Dienst
 *
 */
public class TaggingTest extends OverviewPage {

  /**
   * 
   */
  public TaggingTest() {
    
    super();

    Form<Void> form = new Form<Void>("form")
    {

      private static final long serialVersionUID = 1L;

      @Override
      protected void onSubmit()
      {
        super.onSubmit();

        System.err.println("submitted values: " + get("tagit").getDefaultModelObjectAsString());
      }

    };
    add(form);

    form.add(new TagItTextField<String>("tagit", Model.of("a1, a4"))
    {

      private static final long serialVersionUID = 1L;

      @Override
      protected Iterable<String> getChoices(final String term)
      {

        System.err.println("term> " + term);

        return Arrays.asList("a1", "a2", "a3", "a4");
      }

    });


  }

}
