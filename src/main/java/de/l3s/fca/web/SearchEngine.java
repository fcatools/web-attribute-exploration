package de.l3s.fca.web;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public interface SearchEngine<A> {

	public ResultSet getResults(final Query<A> query);
	
	public ResultSet getResults(final String query);
	
}
