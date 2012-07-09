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
package de.elatexam.editor.preview;

import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.wicket.Session;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;

import com.visural.wicket.component.submitters.IndicateModalLink;

import de.elatexam.editor.util.Stuff;
import de.elatexam.model.ComplexTaskDef;
import de.thorstenberger.taskmodel.TaskModelViewDelegate;
import de.thorstenberger.taskmodel.TaskModelViewDelegateObject;
import de.thorstenberger.taskmodel.complex.ComplexTaskFactory;
import de.thorstenberger.taskmodel.complex.ComplexTaskletCorrectorImpl;
import de.thorstenberger.taskmodel.complex.complextaskdef.impl.ComplexTaskDefDAOImpl;
import de.thorstenberger.taskmodel.complex.complextaskhandling.impl.ComplexTaskHandlingDAOImpl;
import de.thorstenberger.taskmodel.complex.impl.ComplexTaskBuilderImpl;
import de.thorstenberger.taskmodel.complex.impl.ComplexTaskFactoryImpl;
import de.thorstenberger.taskmodel.impl.TaskManagerImpl;
import de.thorstenberger.taskmodel.impl.TaskModelViewDelegateObjectImpl;
import de.thorstenberger.taskmodel.impl.TaskletContainerImpl;
import de.thorstenberger.taskmodel.util.JAXBUtils;

/**
 * Link that calls the locally running taskcore-view webapp to show live preview of a complextaskdef. Will return to the
 * current page afterwards.
 *
 * @author Steffen Dienst
 *
 */
public class PreviewComplexLink extends IndicateModalLink {


  public PreviewComplexLink(final String id, final IModel<ComplexTaskDef> model) {
    super(id);
    setModel(model);
  }

  /**
   * Find external absolute url.
   *
   * @return
   */
  private String getContextUrl() {
    final HttpServletRequest req = ((ServletWebRequest) getRequest()).getContainerRequest();
    final String protocol = req.isSecure() ? "https://" : "http://";
    final String hostname = req.getServerName();
    final int port = req.getServerPort();
    final StringBuffer url = new StringBuffer(128);
    url.append(protocol);
    url.append(hostname);
    if ((port != 80) && (port != 443)) {
      url.append(":");
      url.append(port);
    }
    return url.toString();
  }

  @Override
  public void onClick() {

    final ComplexTaskFactory ctf = new ComplexTaskFactoryImpl(new ComplexTaskletCorrectorImpl());
    final DummyTaskFactoryImpl taskfactory = new DummyTaskFactoryImpl(new ComplexTaskDefDAOImpl(ctf),
        new ComplexTaskHandlingDAOImpl(ctf),new ComplexTaskBuilderImpl(ctf));
    // use currently selected taskmodel (in the tree)
    Marshaller marshaller = null;
    JAXBContext context = Stuff.getContext();
    try {
      // marshal to xml
      marshaller = JAXBUtils.getJAXBMarshaller(context);
      final StringWriter sw = new StringWriter();
      // TODO enable preview for one category, taskblock, subtaskdef
      ComplexTaskDef ctd = (ComplexTaskDef) getModelObject();
      Stuff.makeIDsUnique(ctd);
      marshaller.marshal(ctd, sw);
      // set xml to use
      taskfactory.setTaskDefXml(sw.toString());
      final TaskletContainerImpl taskletContainer = new TaskletContainerImpl(taskfactory);
      // clear internal static caches
      taskletContainer.reset();
      final TaskManagerImpl tm = new TaskManagerImpl(taskfactory, taskletContainer);
      final TaskModelViewDelegateObject delegateObject = new TaskModelViewDelegateObjectImpl(
    		  0,
    		  tm,
    		  "sampleUser", "Max Mustermann",
          getReturnLink());
      TaskModelViewDelegate.storeDelegateObject(getSession().getId(), 0, delegateObject);
      System.out.println(getContextUrl()+" | "+getReturnLink());
      getRequestCycle().scheduleRequestHandlerAfterCurrent(
          new RedirectRequestHandler(getContextUrl() + "/taskmodel-core-view/execute.do?id=0&todo=new&try=" + tryId.incrementAndGet()));
    } catch (final JAXBException e) {
      e.printStackTrace();
    }finally{
    	if(marshaller!=null)
    		JAXBUtils.releaseJAXBMarshaller(context, marshaller);
    }
  }

    /**
     * Create return link to this very page instance. Clear TaskModelViewDelegate on return.
     *
     * @return
     */
    private String getReturnLink() {
      String rel= (String) urlFor(new RenderPageRequestHandler(new PageProvider(getPage())){
        @Override
        public void respond(IRequestCycle requestCycle) {
          // remove preview task
          TaskModelViewDelegate.removeSession(Session.get().getId());
          super.respond(requestCycle);
        }
        
      });
      final HttpServletRequest req = ((ServletWebRequest) getRequest()).getContainerRequest();
      return RequestUtils.toAbsolutePath(req.getRequestURL().toString(), rel);
    }

    private static AtomicLong tryId = new AtomicLong(0);
}