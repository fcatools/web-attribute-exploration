package de.l3s.fca.web;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.l3s.fca.io.SimpleHttpClient;
import de.l3s.fca.util.StringUtil;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class Bing<A> implements SearchEngine<A> {

	private static final Log log = LogFactory.getLog(Bing.class);

	private String accountKey;
	private static final String BASE_URL = "https://api.datamarket.azure.com/Bing/Search/Web";

	private final SimpleHttpClient client;


	public Bing() {
		this.client = new SimpleHttpClient();
	}

	@Override
	public ResultSet getResults(final Query<A> query) {
		return getResults(query.getQueryString());
	}

	public ResultSet getResults(final String queryString) {
		log.info("querying Bing with " + queryString);
		/*
		 * format = json
		 * top = 10
		 * Options = 'EnableHighlighting' TODO: insert &Options='EnableHighlighting'
		 */
		final String url = BASE_URL + "?$format=json&$top=10&Query=" + StringUtil.escape("'" + queryString + "'");

		
		try {
			final String response = this.client.getContent(new URL(url), accountKey, accountKey);

			final ResultSet resultSet = parseResult(response);

			log.info("got " + resultSet.getResults().size() + " of " + resultSet.getResultCount() + " results");
			return resultSet;
		} catch (IOException e) {
			log.error("Could not get results from Bing.", e);
		} catch (URISyntaxException e) {
			log.error("Could not get results from Bing.", e);
		}
		return null;
	}

	protected ResultSet parseResult(final String response) throws MalformedURLException {
		final ResultSet resultSet = new ResultSet();
		final List<Result> results = resultSet.getResults();

		final JSONArray array = (JSONArray) ((JSONObject)((JSONObject)JSONValue.parse(response)).get("d")).get("results");

		for (int i = 0; i < array.size(); i++) {
			final JSONObject hit = (JSONObject) array.get(i);
			
			final Result result = new Result();
			result.setUrl(new URL((String) hit.get("Url")));
			result.setTitle((String) hit.get("Title"));
			result.setSnippet((String) hit.get("Description"));
			
			results.add(result);
		}

		return resultSet;
	}

	public String getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
	}

}
