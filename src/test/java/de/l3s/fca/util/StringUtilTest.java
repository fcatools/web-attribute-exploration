package de.l3s.fca.util;

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
public class StringUtilTest {

	@Test
	public void testShortenUrl() throws MalformedURLException {
		assertEquals("http://www.example.com/", s(20, "http://www.example.com/"));
		assertEquals("http://www.example.com/.../file.html", s(40, "http://www.example.com/path/to/file.html"));
		
	}

	private String s(final int l, final String u) throws MalformedURLException {
		return StringUtil.shortenUrl(new URL(u), l);
	}

	@Test
	public void testFormat() {
		final String input = "This is a good test example.";
		assertEquals("  This\n  is a\n  good\n  test\n  exam\n  ple.", StringUtil.format(input, 6, 2));
		assertEquals("  This is a good test example.", StringUtil.format(input, 30, 2));
		assertEquals("  This is a good\n  test example.", StringUtil.format(input, 16, 2));
	}
	
	@Test
	public void testWs() {
		assertEquals("", StringUtil.ws(0));
		assertEquals(" ", StringUtil.ws(1));
		assertEquals("  ", StringUtil.ws(2));
	}

	@Test
	public void testAlign() {
		assertEquals(" 3", StringUtil.align(3, 2));
		assertEquals("3", StringUtil.align(3, 1));
		assertEquals("33", StringUtil.align(33, 1));
		assertEquals("  3", StringUtil.align(3, 3));
	}
}

