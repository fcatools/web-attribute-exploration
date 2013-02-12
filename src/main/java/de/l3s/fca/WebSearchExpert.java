package de.l3s.fca;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.l3s.fca.util.PowerSet;
import de.l3s.fca.util.StringUtil;
import de.l3s.fca.web.Query;
import de.l3s.fca.web.Result;
import de.l3s.fca.web.ResultSet;
import de.l3s.fca.web.SearchEngine;
import de.tudresden.inf.tcs.fcaapi.FCAImplication;
import de.tudresden.inf.tcs.fcaapi.FCAObject;
import de.tudresden.inf.tcs.fcalib.AbstractExpert;
import de.tudresden.inf.tcs.fcalib.FormalContext;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class WebSearchExpert<A> extends AbstractExpert<A, String, FCAObject<A,String>> {

	private final FormalContext<A,A> context;
	private final SearchEngine<A> searchEngine;
	private A domain;
	private String site;

	
	public WebSearchExpert (final FormalContext<A,A> context, final SearchEngine<A> searchEngine) {
		this.context = context;
		this.searchEngine = searchEngine;
	}
	
	@Override
	public void askQuestion(final FCAImplication<A> implication) {
		System.out.println("Is it true that " + implication + " holds?");
	
		final Set<A> premise = implication.getPremise();
		final Set<A> conclusion = implication.getConclusion();
		
		for (final Set<A> negativeAttributes : new PowerSet<A>(conclusion)) {
			if (negativeAttributes.size() == 0) continue;
			
			final Query<A> query = new Query<A>();
			query.setDomain(domain);
			query.setSite(site);
			
			final List<A> positiveAttributes = new LinkedList<A>(premise);
			final Set<A> inverseSet = new HashSet<A>(conclusion);
			inverseSet.removeAll(negativeAttributes);
			positiveAttributes.addAll(inverseSet);

			
			query.setPositiveTerms(positiveAttributes);
			query.setNegativeTerms(negativeAttributes);
			
			final ResultSet results = searchEngine.getResults(query);
			System.out.println("Results (" + results.getResults().size() + " of " + results.getResultCount() + ")");
			System.out.println(StringUtil.repeat('-', 80));
			printResults(results.getResults());
			System.out.println(StringUtil.repeat('-', 80));
			System.out.println("Do these results contain a counterexample that refutes the implication (yes/no)?");
			// TODO: read user input
			final String answer = "yes";
			if ("yes".equals(answer)) {
				// get counter example
				
			}
		}
	}

	public void printResults(final List<Result> results) {
		int i = 1;
		final int width = (results.size() + "").length();
		for (final Result result : results) {
			System.out.println(StringUtil.align(i++, width) + ". " + result);
		}
	}
	
	@Override
	public void counterExampleInvalid(FCAObject<A, String> arg0, int arg1) {
		System.out.println("The given counterexample is invalid");
		
	}

	@Override
	public void forceToCounterExample(FCAImplication<A> arg0) {
		System.out.println("forceToCounterExample");
	}

	@Override
	public void requestCounterExample(FCAImplication<A> arg0) {
		System.out.println("requestCounterExample");
	}

	@Override
	public void explorationFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void implicationFollowsFromBackgroundKnowledge(FCAImplication<A> arg0) {
		// TODO Auto-generated method stub
		
	}

	public A getDomain() {
		return domain;
	}

	public void setDomain(A domain) {
		this.domain = domain;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}


}
