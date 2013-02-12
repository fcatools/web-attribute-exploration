package de.l3s.fca.io;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class SimpleHttpClient {
	private static final Log log = LogFactory.getLog(SimpleHttpClient.class);

	private final DefaultHttpClient client;

	//	private static final String USER_AGENT = "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2.13) Gecko/20101206 Ubuntu/10.10 (maverick) Firefox/3.6.13";
	private static final String USER_AGENT = "Dillo/0.8.6";

	public SimpleHttpClient() {
		this.client = new DefaultHttpClient();
		this.client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
	}



	public String getContent(final URL url, final String user, final String pass) throws URISyntaxException, ClientProtocolException, IOException {
		final HttpGet get = new HttpGet(url.toURI());
		final BasicHttpContext localcontext = new BasicHttpContext();

		if (user != null) {
			final HttpHost targetHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
			this.client.getCredentialsProvider().setCredentials(
					new AuthScope(targetHost.getHostName(), targetHost.getPort()),
					new UsernamePasswordCredentials(user, pass)
			);

			// Create AuthCache instance
			final AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local
			// auth cache
			final BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);

			// Add AuthCache to the execution context
			localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);
			//			get.setHeader("Authorization", "Basic " + Base64.encodeBase64URLSafeString((user + ":" + pass).getBytes("UTF-8")));
		}

		log.info("executing request " + get.getURI());

		final BasicResponseHandler responseHandler = new BasicResponseHandler();

		final String responseBody = this.client.execute(get, responseHandler, localcontext);

		log.info("response.length = " + ((responseBody == null) ? 0 : responseBody.length()));

		return responseBody;
	}

	public String getContent(final URL url) throws URISyntaxException, ClientProtocolException, IOException {
		return getContent(url, null, null);
	}

}
