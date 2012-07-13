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
package de.elatexam.editor;

import java.io.File;

import net.databinder.web.DataServer;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.deployer.WebAppDeployer;
import org.mortbay.jetty.handler.MovedContextHandler;
import org.mortbay.jetty.servlet.HashSessionManager;
import org.mortbay.jetty.servlet.SessionHandler;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * @author Steffen Dienst
 * 
 */
public class Startup extends DataServer {

  /**
   * @param args
   */
  public static void main(final String[] args) {
    final File dbLockfile = new File("target/database.lck");
    if (dbLockfile.exists()) {
      dbLockfile.delete();
    }

    new Startup();

  }

  @Override
  protected void configure(final Server server, final WebAppContext context) throws Exception {
    super.configure(server, context);
    for (final Handler h : server.getHandlers()) {
      if (h instanceof MovedContextHandler) {
        server.removeHandler(h);
      }
    }
    //
    // make sure we find extra dependencies
    context.setParentLoaderPriority(true);

    final WebAppDeployer wad = new WebAppDeployer();
    wad.setContexts(server);
    wad.setWebAppDir("target/preview");
    // wad.setExtract(true);
    wad.start();
    
    HashSessionManager sm = new HashSessionManager();
    // use empty session path to make sure, all webapps share the session id
    // this is needed for data exchange via TaskModelViewDelegate
    sm.setSessionPath("/");
    File sessionsDir = new File("target/jetty-sessions");
    sessionsDir.mkdirs();
    sm.setStoreDirectory(sessionsDir);
    sm.setSavePeriod(120);
    context.setSessionHandler(new SessionHandler(sm));
    
  }
}
