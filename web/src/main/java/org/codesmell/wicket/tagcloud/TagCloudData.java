/**
 * 
 */
package org.codesmell.wicket.tagcloud;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.link.AbstractLink;

/**
 * Contains the necessary Data for rendering a TagCloud
 * @author Uwe Schäfer, (uwe@codesmell.org)
 *
 */

public abstract class TagCloudData implements Serializable
{
    private List<TagData> tags;

    private int minW = 0;
    private int maxW = 0;

    private int[] sizes;

    private final int maxFontSize;

    private final int minFontSize;

    /**
     * @param tags A list of TagData objects
     * minFontSize defaults to 10, maxFontSize defaults to 36
     */
    public TagCloudData(final List<TagData> tags)
    {
        this(tags, 10, 36);
    }

    /**
     * @param tags A list of TagData objects 
     * @param minFontSize the min size in Pixels for a font (must be larger than 2)
     * @param maxFontSize the max size in Pixels for a font (must be less than 100)
     */
    public TagCloudData(final List<TagData> tags, final int minFontSize, final int maxFontSize)
    {
        Assert.parameterInRange(minFontSize, 2, 50, "minFontSize");
        Assert.parameterInRange(maxFontSize, minFontSize + 1, 100, "maxFontSize");
        Assert.parameterNotNull(tags, "tags");

        this.minFontSize = minFontSize;
        this.maxFontSize = maxFontSize;
        this.tags = Collections.unmodifiableList(tags);
        for (TagData tag : tags)
        {
            int w = tag.getWeight();
            if (minW == 0)
            {
                minW = w;
            }
            else
            {
                minW = Math.min(minW, w);
            }
            maxW = Math.max(maxW, w);
        }
        sizes = new int[maxW - minW + 1];
    }

    public final List<TagData> getTags()
    {
        return tags;
    }

    final int getMaxWeight()
    {
        return maxW;
    }

    final int getMinWeight()
    {
        return minW;
    }

    protected synchronized int weightToPixel(final int weight)
    {
        int pos = weight - minW;
        int w = sizes[pos];
        if (w == 0)
        {
            w = Math.abs((maxFontSize - minFontSize) * (weight - minW) / Math.max(1,maxW - minW)) + minFontSize;
            sizes[pos] = w;
        }
        return w;
    }

	protected abstract AbstractLink getLink(String id,TagData modelObject) ;
}
