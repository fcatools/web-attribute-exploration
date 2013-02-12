package de.l3s.fca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.l3s.fca.web.Query;
import de.tudresden.inf.tcs.fcaapi.FCAImplication;
import de.tudresden.inf.tcs.fcaapi.FCAObject;
import de.tudresden.inf.tcs.fcaapi.action.ExpertAction;
import de.tudresden.inf.tcs.fcalib.AbstractContext;
import de.tudresden.inf.tcs.fcalib.AbstractExpert;
import de.tudresden.inf.tcs.fcalib.FullObject;
import de.tudresden.inf.tcs.fcalib.action.StartExplorationAction;

/**
 * @author rja
 * @version $Id:$
 */
public class AsyncExpert<A> extends AbstractExpert<A, String, FCAObject<A,String>> {

	private static final Log log = LogFactory.getLog(AsyncExpert.class);

	/**
	 * A monitor object that allows us to wait until a question is available.
	 */
	private final Object questionAvailable = new Object();
	/**
	 * Holds the current question
	 */
	private FCAImplication<A> question;
	/**
	 * Holds the incoming answers from the controller
	 */
	private final BlockingQueue<ExpertAction> actionQueue = new LinkedBlockingQueue<ExpertAction>(1); 

	private List<Query<A>> queries;

	private boolean explorationRunning = false;
	
	private A domain;
	private String site;

	/**
	 * Starts the exploration as a background process. 
	 * 
	 * @param context
	 */
	public void startExploration(final AbstractContext<A,String,FullObject<A,String>> context) throws InterruptedException {
		// Create an expert action for starting attribute exploration           
		final StartExplorationAction<A,String,FullObject<A,String>> action =  new StartExplorationAction<A,String,FullObject<A,String>>();
		action.setContext(context);

		/*
		 * Since fireExpertAction() blocks until the attribute exploration is 
		 * finished, we must call it in a separate thread. 
		 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				explorationRunning = true;
				// Fire the action, exploration starts...
				fireExpertAction(action); // TODO: do async
				// finished: remove question
				question = null;
				explorationRunning = false;
			}
		}, "attribute exploration").start();

		/*
		 * wait until a question becomes available for the calling thread
		 */
		synchronized (this.questionAvailable) {
			this.questionAvailable.wait(1000 * 10); // wait at most ten seconds 
		}
	}


	@Override
	public void askQuestion(final FCAImplication<A> question) {
		log.info("a new question is asked");
		try {
			this.question = question;
			this.queries = getQueries(question);	
			// notify the thread that called getQueries() or getQuestion()
			// that a new question is available
			log.info("queries are now available");
			synchronized (this.questionAvailable) {
				this.questionAvailable.notifyAll();	
			}
			System.out.println("Is it true that " + question + " holds?");

			fireExpertAction(this.actionQueue.take());
		} catch (final InterruptedException e) {
			log.error("Could not get answer.", e);
			throw new RuntimeException("Could not get answer.", e);
		}
	}

	private List<Query<A>> getQueries(final FCAImplication<A> question) {
		final List<Query<A>> queries = new ArrayList<Query<A>>(); 
		final Set<A> premise = question.getPremise();
		final Set<A> conclusion = question.getConclusion();

		for (final A negativeAttribute : conclusion) {

			final Query<A> query = new Query<A>();
			query.setDomain(this.domain);
			query.setSite(this.site);

			query.setPositiveTerms(premise);
			query.setNegativeTerms(Collections.singletonList(negativeAttribute));

			/*
			 * put query into buffer
			 */
			queries.add(query);
		}
		return queries;
	}		


	@Override
	public void counterExampleInvalid(FCAObject<A, String> arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void forceToCounterExample(FCAImplication<A> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestCounterExample(FCAImplication<A> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void explorationFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public void implicationFollowsFromBackgroundKnowledge(FCAImplication<A> arg0) {
		// TODO Auto-generated method stub

	}

	public FCAImplication<A> getQuestion() {
		return this.question;
	}

	public void addAction(final ExpertAction action) {
		/*
		 * We have a race condition in this method: if we don't wait until a new
		 * question is available, the caller gets old queries upon calling 
		 * getQueries() afterwards. On the other hand, often the thread doing
		 * the exploration is calling questionAvailable().notify() before we 
		 * start to wait on it and therefore wait until the timeout is reached.
		 * 
		 * As a workaround, we set the queries to null and only wait, if they
		 * have not been filled, yet.
		 */

		try {
			this.queries = null;
			log.info("adding answer");
			this.actionQueue.put(action);
			synchronized (this.questionAvailable) {
				if (this.queries == null && this.explorationRunning) {
					log.info("waiting for the next question to become available");
					this.questionAvailable.wait(1000 * 10); // wait at most ten seconds 
				}
			}
		} catch (InterruptedException e) {
			log.error("Could not add answer.", e);
			throw new RuntimeException("Could not add answer.", e);
		}
	}


	public void setDomain(A domain) {
		this.domain = domain;
	}


	public void setSite(String site) {
		this.site = site;
	}


	public List<Query<A>> getQueries() {
		return queries;
	}

}
