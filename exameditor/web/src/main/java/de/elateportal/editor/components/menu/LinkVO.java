package de.elateportal.editor.components.menu;

import java.io.Serializable;

import org.apache.wicket.markup.html.WebPage;

/**
 * Simple bean with link informations.
 * 
 * @author Steffen Dienst
 */
public class LinkVO implements Serializable {
    public interface Create extends Serializable {
        WebPage createPage();
    }

    /**
     * 
     */
    private static final long serialVersionUID = 7059147735653473097L;

    private Class<? extends WebPage> responsePageClass;
    private WebPage responsePage;
    private String linkText = null;

    private String externalUrl;

    private Create pageCallback;

    /**
     */
    public LinkVO(final Class<? extends WebPage> linkPageClass, final String linkText) {
        this.responsePageClass = linkPageClass;
        this.linkText = linkText;
    }

    public LinkVO(final Create pageCallback, final String linkText) {
        this.pageCallback = pageCallback;
        this.linkText = linkText;
    }

    /**
     */
    public LinkVO(final String externalLink, final String linkText) {
        this.linkText = linkText;
        this.externalUrl = externalLink;
    }

    /**
     */
    public LinkVO(final WebPage linkPage, final String linkText) {
        this.responsePage = linkPage;
        this.linkText = linkText;
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

    public WebPage getResponsePage() {
        if (pageCallback != null) {
            try {
                this.responsePage = pageCallback.createPage();
                this.pageCallback = null;
            } catch (final Exception e) {
                e.printStackTrace();
            }

        }
        return responsePage;
    }

    public Class<? extends WebPage> getResponsePageClass() {
        return responsePageClass;
    }

    public void setLinkText(final String linkText) {
        this.linkText = linkText;
    }
}
