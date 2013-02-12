package de.l3s.fca.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class StringUtil {

	private static final String POINTS = "...";

	public static StringBuilder getString(final InputStream in, final String encoding) throws UnsupportedEncodingException, IOException {
		final StringBuilder buf = new StringBuilder();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));
		String line;
		while ((line = reader.readLine()) != null) {
			buf.append(line).append("\n");
		}
		reader.close();
		return buf;
	}

	public static String escape(final String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	public static String shorten(final String s, final int length) {
		if (s.length() < length) {
			return s;
		}
		return s.substring(0, length - POINTS.length()) + POINTS;
	}

	public static String shortenUrl(final URL url, final int length) {
		final String u = url.toString();
		if (u.length() < length) {
			return u;
		}
		// check up to first ?
		final int indexOfQ = u.indexOf('?');
		final String hostAndPath;
		if (indexOfQ > 0) {
			hostAndPath = u.substring(0, indexOfQ);
			// http://www.example.com/path/file?...
			if (indexOfQ < length - POINTS.length()) {
				return hostAndPath + POINTS;
			}
		} else {
			hostAndPath = u;
		}
		final int indexOfS = hostAndPath.indexOf('/', 9);
		final int lastIndexOfS = hostAndPath.lastIndexOf('/');
		if (indexOfS  + 1 + hostAndPath.length() - lastIndexOfS < length - POINTS.length()) {
			return hostAndPath.substring(0, indexOfS + 1) + POINTS + hostAndPath.substring(lastIndexOfS);
		}
		return u;
	}

	public static String format(final String s, final int width, final int intend) {
		final String ws = ws(intend);
		if (s.length() < width - intend) return ws + s;
		final StringBuilder buf = new StringBuilder();
		int pos = 0;
		while (pos + width - intend < s.length()) {
			buf.append(ws).append(s.substring(pos, pos + width - intend)).append("\n");
			pos += width - intend;
			int indexOfS = s.indexOf(' ', pos);
			while (indexOfS == pos) {
				pos++;
				indexOfS = s.indexOf(' ', pos);
			}
		}
		buf.append(ws).append(s.substring(pos));
		return buf.toString();
	}

	public static String ws(final int length) {
		return repeat(' ', length);
	}
	
	public static String repeat(final char c, final int count) {
		if (count <= 0) return "";
		final char[] a = new char[count];
		Arrays.fill(a, c);
		return new String(a);
	}

	public static String align(final int i, final int width) {
		final int length = (i + "").length();
		return ws(width - length) + i;
	}
	
}
