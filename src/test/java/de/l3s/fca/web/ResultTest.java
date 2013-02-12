package de.l3s.fca.web;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class ResultTest {

	@Test
	public void testToString() throws MalformedURLException {
		final Result result = new Result();
		result.setUrl(new URL("http://www.example.com/very/very/very/very/very/very/long/path/to/cool-file.html"));
		result.setTitle("Example Web Site");
		result.setSnippet("This is the snippet shown under the title which explains why this result is shown.");
		
		final String r = result.toString();
		System.out.println(r);
		assertEquals("Example Web Site\nhttp://www.example.com/.../cool-file.html\n  This is the snippet shown under the title which explains why this result is sh\n  own.", r);
		
	}

}
