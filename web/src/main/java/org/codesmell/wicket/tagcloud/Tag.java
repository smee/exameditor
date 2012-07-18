/**
 * 
 */
package org.codesmell.wicket.tagcloud;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * 
 * @author Uwe Schäfer, (uwe@codesmell.org)
 *
 */
class Tag extends Panel
{
    private final TagData td;
    
    private final int size;

	private AbstractLink link;

    /**
     * @param string
     * @param modelObject
     */
    public Tag(final String id, final TagData td, final AbstractLink link, final int size)
    {
        super(id);
        this.td = td;
        this.link = link;
        this.size = size;

    }

    /* (non-Javadoc)
     * @see org.apache.wicket.Component#onBeforeRender()
     */
    @Override
    protected void onBeforeRender()
    {
        super.onBeforeRender();

        if (!hasBeenRendered())
        {
            link.add(new Label("l", td.getName()));
            link.add(new SimpleAttributeModifier("style", "font-size: " + size + "px;"));
            add(link);
        }
    }

}
