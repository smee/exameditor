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
package de.elatexam.editor.components.form;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * @author Steffen Dienst
 *
 */
public class EnhanceFormsListener implements IComponentInstantiationListener {

    /* (non-Javadoc)
     * @see org.apache.wicket.application.IComponentInstantiationListener#onInstantiation(org.apache.wicket.Component)
     */
    @Override
    public void onInstantiation(Component component) {
        if (component instanceof FormComponent) {
            ((FormComponent) component).add(new ErrorHighlightBehavior());
        }
    }

}
