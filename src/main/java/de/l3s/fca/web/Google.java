package de.l3s.fca.web;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.l3s.fca.io.SimpleHttpClient;
import de.l3s.fca.util.FileUtil;
import de.l3s.fca.util.StringUtil;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class Google<A> implements SearchEngine<A> {

	private static final Log log = LogFactory.getLog(Google.class);
	
	private final SimpleHttpClient client;
	private static final String BASE_URL = "http://www.google.com/";
	
	private static final String GOOGLE_URL_PREFIX = "/url?q=";
	private static final Pattern TOTAL_HIT_PATTERN = Pattern.compile("<div id=\"resultStats\">.+?([0-9,]+).+?</div>");
	private static final Pattern LIST_ELEMENT_PATTERN = Pattern.compile("<li class=\"g\">(.+?)</li>");
	private static final Pattern HIT_URL_PATTERN = Pattern.compile("<h3 class=\"r\"><a href=\"(.+?)\".*?>(.+?)</a>");
	private static final Pattern HIT_SNIPPET_PATTERN = Pattern.compile("<span class=\"st\">(.+?)</span>"); 

	public Google() {
		this.client = new SimpleHttpClient();
	}

	@Override
	public ResultSet getResults(final Query<A> query) {
		return getResults(query.getQueryString());
	}

	@Override
	public ResultSet getResults(final String queryString) {
		log.info("querying Google with " + queryString);
		final String url = BASE_URL + "?q=" + StringUtil.escape(queryString);

		try {


			if ("2".equals("1")) {
				// dummy for testing
				final ResultSet r = new ResultSet();
				r.setResultCount(42);
				final LinkedList<Result> l = new LinkedList<Result>();
				final Result r1 = new Result();
				r1.setUrl(new URL("http://www.example.com/with/a/really/very/very/very/very/long/path/to/a/cool-file.html"));
				r1.setTitle("Example Web Page");
				r1.setSnippet("This is the snippet that is shown as indicator for the relevance of the document.");
				l.add(r1);
				r.setResults(l);
				return r;
			};


			// The underlying HTTP connection is still held by the response object 
			// to allow the response content to be streamed directly from the network socket. 
			// In order to ensure correct deallocation of system resources 
			// the user MUST either fully consume the response content  or abort request 
			// execution by calling HttpGet#releaseConnection().


			final String response = this.client.getContent(new URL(url));


			final ResultSet googleResult = parseResult(response);
			
			log.info("got " + googleResult.getResults().size() + " of " + googleResult.getResultCount() + " results");
			return googleResult;
		} catch (IOException e) {
			log.error("Could not get results from Google.", e);
		} catch (URISyntaxException e) {
			log.error("Could not get results from Google.", e);
		}

		return null;
	}
	
	

	private ResultSet parseResult(final String queryResult) {
		final ResultSet resultSet = new ResultSet();
		if (queryResult != null) {
			
			
			try {
				final BufferedWriter writer = FileUtil.getWriter("/tmp/result.html");
				writer.write(queryResult);
				writer.close();
			} catch (Exception e) {
				System.err.println("could not write file");
			}
			
			/*
			 * parse hits
			 */
			final Matcher liMatcher = LIST_ELEMENT_PATTERN.matcher(queryResult);
			while (liMatcher.find()) {
				final String hit = liMatcher.group(1);
				final Matcher urlMatcher = HIT_URL_PATTERN.matcher(hit);
				if (urlMatcher.find()) {
					try {
						final URL url = new URL(cleanUrl(urlMatcher.group(1)));
						final Result result = new Result();
						result.setUrl(url);
						result.setTitle(urlMatcher.group(2));
						
						final Matcher snippetMatcher = HIT_SNIPPET_PATTERN.matcher(hit);
						if (snippetMatcher.find()) {
							result.setSnippet(snippetMatcher.group(1));
						}
						
						resultSet.getResults().add(result);
					} catch (MalformedURLException e) {
						log.error("Could not create URL.", e);
					}
				}
			}
			/*
			 * get total number of results
			 */
			final Matcher matcherTotal = TOTAL_HIT_PATTERN.matcher(queryResult);
			if (matcherTotal.find()) {
				resultSet.setResultCount(Integer.parseInt(matcherTotal.group(1).replaceAll("[\\.,]", "")));
			}
		}
		return resultSet;
	}
	


	private String cleanUrl(final String url) {
		if (url.startsWith(GOOGLE_URL_PREFIX)) {
			return decode(url.substring(GOOGLE_URL_PREFIX.length(), url.indexOf("&amp;")));
		} else {
			return url;
		}
	}

	private static String decode(final String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}
	
}
