package de.l3s.fca.web;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

import de.l3s.fca.util.FileUtil;

/**
 * @author rja
 * @version $Id:$
 */
public class BingTest {

	@Test
	@Ignore
	public void testGetResultsString() {
		final Bing<String> bing = new Bing<String>();
		final ResultSet results = bing.getResults("'Hello World'");
		
		System.out.println(results);
	}

	@Test
	public void testParseResults() throws UnsupportedEncodingException, IOException {
		final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("bing_result.json");
		
		final StringBuilder content = FileUtil.getContent(stream);
		
		final Bing bing = new Bing();
		
		final ResultSet result = bing.parseResult(content.toString());
		
		assertEquals(50, result.getResults().size());

		assertEquals(new URL("http://de.wikipedia.org/wiki/Hallo-Welt-Programm"), result.getResults().get(0).getUrl());
		
	}
	
}
