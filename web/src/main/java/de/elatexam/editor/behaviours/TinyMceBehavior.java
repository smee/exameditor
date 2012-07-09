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
package de.elatexam.editor.behaviours;

import java.io.File;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.protocol.http.WebRequest;

import wicket.contrib.tinymce.settings.TinyMCESettings;

/**
 * @author Steffen Dienst
 *
 */
public class TinyMceBehavior extends wicket.contrib.tinymce.TinyMceBehavior {

  /**
   * 
   */
  public TinyMceBehavior() {
    // TODO Auto-generated constructor stub
  }

  /**
   * @param settings
   */
  public TinyMceBehavior(TinyMCESettings settings) {
    super(settings);
    // TODO Auto-generated constructor stub
  }
  private boolean mayRenderJavascriptDirect() {
    return RequestCycle.get().getRequest() instanceof WebRequest && !((WebRequest)RequestCycle.get().getRequest()).isAjax();
}
  public void renderHead(IHeaderResponse response) {
//    super.renderHead(response);

    // TinyMce javascript:
    if (mayRenderJavascriptDirect())
      response.renderJavascriptReference(TinyMCESettings.javaScriptReference());
    else
      lazyLoadTinyMCEResource(response);
    String renderOnDomReady = getRenderOnDomReadyJavascript(response);
    if (renderOnDomReady != null)
        response.renderOnDomReadyJavascript(renderOnDomReady);

    String renderJavaScript = getRenderJavascript(response);
    if (renderJavaScript != null)
        response.renderJavascript(renderJavaScript, null);
}
  
  public static void lazyLoadTinyMCEResource(IHeaderResponse response) {
    
    String url = RequestCycle.get().urlFor(TinyMCESettings.javaScriptReference()).toString();
    String base = url.substring(0, url.lastIndexOf("/"));
    response.renderJavascript("window.tinyMCEPreInit = {base : '" + base + "', suffix : '', query : ''};", "tinyMceHackPreload");
    response.renderJavascriptReference(TinyMCESettings.javaScriptReference());
    response.renderJavascript("window.tinymce.dom.Event.domLoaded = true;", "tinyMceHackPostload");
  }
}
