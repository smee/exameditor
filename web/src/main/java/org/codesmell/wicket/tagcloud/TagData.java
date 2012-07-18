/**
 * 
 */
package org.codesmell.wicket.tagcloud;

import java.io.Serializable;

/**
 * Encapsulates a Tag and its weight
 * 
 * @author Uwe Schäfer, (uwe@codesmell.org)
 *
 */
public final class TagData implements Serializable
{
    private final int weigth;
    private final String name;

    /**
     * @param name the String to be displayed
     * @param weigth the weigth (most likely occurrences) of the given tag
     */
    public TagData(final String name, final int weigth)
    {
        Assert.parameterNotNull(name, "name");
        Assert.parameterNotNull(weigth, "weigth");
        this.name = name;
        this.weigth = weigth;
    }

    public final String getName()
    {
        return name;
    }

    final int getWeight()
    {
        return weigth;
    }
}
