package de.l3s.fca.web;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class ResultSet {

	private int resultCount;
	private List<Result> results = new LinkedList<Result>();
	
	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
	
	@Override
	public String toString() {
		return results.toString();
	}
}
