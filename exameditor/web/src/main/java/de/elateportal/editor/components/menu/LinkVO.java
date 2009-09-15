package de.elateportal.editor.components.menu;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;

/**
 * Simple bean with link informations.
 * 
 * @author Steffen Dienst
 */
public class LinkVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7059147735653473097L;

    private final Class<? extends WebPage> responsePage;
    private String linkText = null;

    private final String externalUrl;

    /**
     * @param linkPageClass
     * @param linkText
     */
    public LinkVO(final Class<? extends WebPage> linkPageClass, final String linkText) {
        this.responsePage = linkPageClass;
        this.linkText = linkText;
        this.externalUrl = null;
    }

    /**
     * @param externalLink
     * @param linkText
     */
    public LinkVO(final String externalLink, final String linkText) {
        this.linkText = linkText;
        this.externalUrl = externalLink;
        this.responsePage = null;
    }

    /**
     * @return the externalUrl
     */
    public String getExternalUrl() {
        return externalUrl;
    }

    public String getLinkText() {
        return linkText;
    }

    public Class<? extends WebPage> getResponsePage() {
        return responsePage;
    }

    public void setLinkText(final String linkText) {
        this.linkText = linkText;
    }

}
