package de.fhdw.wipbank.server.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author Philipp Dyck
 * @author Daniel Sawenko
 */
public class JettyServer {

	/**
	 * Startet den Jetty Server
	 *
	 * @throws Exception
	 */
	public static void run() throws Exception {
		Server server = new Server(9998);

		Handler restHandler = buildRestHandler();
        Handler angularHandler = buildAngularHandler();
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { restHandler, angularHandler });
		server.setHandler(handlers);

		server.start();
		
		
		//Konfiguration des Loggers
		Logger logger = Logger.getRootLogger();
		//SimpleLayout layout = new SimpleLayout();
		PatternLayout layout = new PatternLayout( "%d{ISO8601} %-5p [%t] %c: %m%n" );
		ConsoleAppender appender = new ConsoleAppender(layout);
		logger.addAppender(appender);
		logger.setLevel(Level.ALL);
		FileAppender fileAppender = new FileAppender( layout, "logs/MeineLogDatei.log", false );
	    logger.addAppender( fileAppender );
	}

	/**
	 * Setup für die REST API
	 *
	 * @return
	 */
	private static Handler buildRestHandler() {
		ResourceConfig resourceConfig = new PackagesResourceConfig("de.fhdw.wipbank.server.rest");
		ServletContextHandler sh = new ServletContextHandler();
		sh.setContextPath("/rest");
		sh.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");
		return sh;
	}

	/**
	 * Setup für die Angular App
	 *
	 * @return
	 */
	private static Handler buildAngularHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase("de.fhdw.wipbank.client.angular");
        ContextHandler contextHandler = new ContextHandler("/angular");
        contextHandler.setHandler(resourceHandler);
        return contextHandler;
    }
}
