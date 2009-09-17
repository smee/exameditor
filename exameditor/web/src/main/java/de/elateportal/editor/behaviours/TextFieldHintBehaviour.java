package de.elateportal.editor.behaviours;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
                    
/**
 * Adds hint-text to texfield, that will be erased onFocus
 * @author igor vaynberg
 */
public class TextFieldHintBehaviour extends AbstractBehavior
{
    private final String pseudoUniqueJavascriptVariableName = this.getClass().getSimpleName()
            + String.valueOf(System.nanoTime());

    public TextFieldHintBehaviour(final IModel hintTextModel)
    {
        this.hint = hintTextModel;
    }

    private final IModel hint;
    private Component c;

    private String hintColor = "gray";
    private String textColor = "black";

    @Override
    public void bind(final Component component)
    {
        super.bind(component);
        this.c = component;
        this.c.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(final IHeaderResponse response)
    {
        response.renderOnDomReadyJavascript("var " + this.pseudoUniqueJavascriptVariableName
                + "=document.getElementById('" + this.c.getMarkupId() + "');" + this.pseudoUniqueJavascriptVariableName
                + ".value='" + this.hint.getObject() + "';" + this.pseudoUniqueJavascriptVariableName
                + ".style['color']='" + this.hintColor + "';");
    }

    @Override
    public void onComponentTag(final Component component, final ComponentTag tag)
    {
        super.onComponentTag(component, tag);
        tag.put("onfocus", "if (this.value=='" + this.hint.getObject() + "') {this.value=''; this.style['color']='"
                + this.textColor + ";'}");
    }

    public String getHintColor()
    {
        return this.hintColor;
    }

    public void setHintColor(final String hintColor)
    {
        this.hintColor = hintColor;
    }

    public String getTextColor()
    {
        return this.textColor;
    }

    public void setTextColor(final String textColor)
    {
        this.textColor = textColor;
    }
}
