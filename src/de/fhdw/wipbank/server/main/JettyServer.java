package de.fhdw.wipbank.server.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class JettyServer {

	public static void run() throws Exception {
		Server server = new Server(9998);

		ResourceConfig resourceConfig = new PackagesResourceConfig("de.fhdw.wipbank.server.rest");
		ServletContextHandler sh = new ServletContextHandler();
		sh.setContextPath("/rest");
		sh.addServlet(new ServletHolder(new ServletContainer(resourceConfig)), "/*");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { sh });
		server.setHandler(handlers);

		server.start();
	}
}
