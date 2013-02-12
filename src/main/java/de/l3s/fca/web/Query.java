package de.l3s.fca.web;

import java.util.Collection;

import de.l3s.fca.util.ValidationUtils;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class Query<A> {

	private Collection<A> positiveTerms;
	private Collection<A> negativeTerms;
	private A domain;
	private String site;
	
	public Collection<A> getPositiveTerms() {
		return positiveTerms;
	}
	public void setPositiveTerms(final Collection<A> positiveTerms) {
		this.positiveTerms = positiveTerms;
	}
	public Collection<A> getNegativeTerms() {
		return negativeTerms;
	}
	public void setNegativeTerms(final Collection<A> negativeTerms) {
		this.negativeTerms = negativeTerms;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public A getDomain() {
		return domain;
	}
	public void setDomain(A domain) {
		this.domain = domain;
	}
	
	public String getQueryString() {
		final StringBuilder buf = new StringBuilder();

		if (ValidationUtils.present(getDomain()) && ValidationUtils.present(getDomain().toString())) {
			buf.append("+\"" + getDomain() + "\" ");
		}

		for (final A positive : getPositiveTerms()) {
			buf.append("+\"" + positive + "\" ");
		}

		for (final A negative : getNegativeTerms()) {
			buf.append("-\"" + negative + "\" ");
		}

		if (ValidationUtils.present(getSite())) {
			buf.append("site:" + getSite());
		}


		return buf.toString();
	}
	
}
