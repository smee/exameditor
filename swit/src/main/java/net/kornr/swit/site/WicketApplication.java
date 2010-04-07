/*
 * Copyright 2009 Rodrigo Reyes reyes.rr at gmail dot com
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package net.kornr.swit.site;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import net.kornr.swit.button.ButtonResource;
import net.kornr.swit.site.buttoneditor.ButtonEditor;
import net.kornr.swit.site.quickstart.QuickStart;
import net.kornr.swit.util.StringUtils;
import net.kornr.swit.wicket.border.graphics.BorderMaker;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.session.ISessionStore;
import org.slf4j.LoggerFactory;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see net.kornr.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{    
    /**
     * Constructor
     */
	public WicketApplication()
	{
	}
	
	@Override
	protected void init() {
		super.init();
		
		ButtonResource.cleanUpFiles();
		BorderMaker.install("borders-engine");

		this.mountBookmarkablePage("/Quickstart", QuickStart.class);
		this.mountBookmarkablePage("/ButtonEditor", ButtonEditor.class);
		this.mountBookmarkablePage("/Download", Download.class);
		
		ConvertUtils.register(new ColorConverter(), Color.class);
	}

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	public Class<HomePage> getHomePage()
	{
		return HomePage.class;
	}

	@Override
	protected void onDestroy() {
		ButtonResource.cleanUpFiles();
	}

	@Override
	public String getConfigurationType() 
	{
		String hasLicense = StringUtils.load(Download.class, "/SHORT-LICENSE", null);

		if (hasLicense == null)
			return Application.DEVELOPMENT;
		else
			return Application.DEPLOYMENT;
	}

	

}
