package de.l3s.fca;

import org.eclipse.jetty.webapp.WebAppContext;


/**
 * @author rja
 * @version $Id:$
 */
public class Server {

	public static void main(final String[] args) throws Exception {
		final int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8088;
		}
		final org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(port);

		final WebAppContext context = new WebAppContext();
        
        context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        context.setResourceBase("src/main/webapp");
        context.setContextPath("/");
        context.setParentLoaderPriority(true);

        server.setHandler(context);
        
		server.start();
		server.join();
	}

}
