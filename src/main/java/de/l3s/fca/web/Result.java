package de.l3s.fca.web;

import java.net.URL;

import de.l3s.fca.util.StringUtil;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class Result {
	private String title;
	private URL url;
	private String snippet;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	
	@Override
	public String toString() {
		return StringUtil.shorten(title, 80) + "\n" + 
		StringUtil.shortenUrl(url, 80) + "\n" + 
		StringUtil.format(snippet, 80, 2); 
	}
}
