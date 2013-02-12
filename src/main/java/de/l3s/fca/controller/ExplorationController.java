package de.l3s.fca.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.l3s.fca.AsyncExpert;
import de.l3s.fca.util.FCAUtil;
import de.l3s.fca.util.ValidationUtils;
import de.l3s.fca.web.Query;
import de.l3s.fca.web.SearchEngine;
import de.tudresden.inf.tcs.fcaapi.Expert;
import de.tudresden.inf.tcs.fcalib.FormalContext;
import de.tudresden.inf.tcs.fcalib.FullObject;
import de.tudresden.inf.tcs.fcalib.action.CounterExampleProvidedAction;
import de.tudresden.inf.tcs.fcalib.action.QuestionConfirmedAction;
import de.tudresden.inf.tcs.fcalib.action.QuestionRejectedAction;

/**
 * @author rja
 * @version $Id:$
 */
@Controller
public class ExplorationController {

	private static final String REDIRECT_EXPLORATION = "redirect:/exploration#exploration";
	private static final String REDIRECT_IMPLICATIONS = "redirect:/exploration#implications";

	private static final String VIEW_EXPLORATION = "/exploration.jspx";

	private static final Log log = LogFactory.getLog(ExplorationController.class);

	@Autowired
	private SearchEngine<String> searchEngine;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/exploration", method = RequestMethod.GET)
	public String startExploration(final HttpSession session) throws InterruptedException {
		final FormalContext<String, String> context = (FormalContext) session.getAttribute("context");
		if (context == null) {
			return "redirect:/";
		}
		log.info("using context:\n " + FCAUtil.toString(context));

		/*
		 * get expert 
		 */
		log.info("getting expert");
		if (context.getExpert() == null) {
			log.info("setting expert");
			final AsyncExpert<String> expert = new AsyncExpert<String>();
			expert.setDomain((String)session.getAttribute("domain"));
			expert.setSite((String)session.getAttribute("site"));

			// Set expert for this context
			context.setExpert((Expert) expert);
			// Context listens to the actions of the expert
			expert.addExpertActionListener(context);

			log.info("starting exploration");
			expert.startExploration(context);

			/*
			 * At this point we must wait until the question and the queries are
			 * computed. This is done 
			 */
			log.info("getting queries");
			setQueries(session, expert.getQueries());
		}

		log.info("returning to view");
		return VIEW_EXPLORATION;
	}


	private void setQueries(final HttpSession session, final List<Query<String>> queries) {
		session.setAttribute("queries", queries);
		session.setAttribute("queryId", 0);
		if (ValidationUtils.present(queries)) {
			final Query<String> currentQuery = queries.get(0);
			session.setAttribute("queryString", currentQuery.getQueryString());
			session.setAttribute("results", this.searchEngine.getResults(currentQuery));
		}
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/exploration/query")
	public String getQuery(@RequestParam(value="queryId", required=false, defaultValue="-1") final int queryId, @RequestParam(value="queryString", required=false) final String queryString, final HttpSession session) throws InterruptedException {

		final FormalContext<String, String> context = (FormalContext) session.getAttribute("context");
		if (context == null) {
			return "redirect:/";
		}

		final AsyncExpert<String> expert = (AsyncExpert) context.getExpert();

		final List<Query<String>> queries = expert.getQueries();

		final String qs;
		if (queryString == null) {
			qs = queries.get(queryId).getQueryString();
		} else {
			qs = queryString;
		}
		session.setAttribute("queryString", qs);
		session.setAttribute("results", this.searchEngine.getResults(qs));

		session.setAttribute("queryId", queryId);
		return REDIRECT_EXPLORATION;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/exploration/confirm", method = RequestMethod.POST)
	public String confirmImplication(final HttpSession session) throws InterruptedException {

		final QuestionConfirmedAction<String, String, FullObject<String,String>> action = new QuestionConfirmedAction<String, String, FullObject<String,String>>();

		final FormalContext<String, String> context = (FormalContext) session.getAttribute("context");
		if (context == null) {
			return "redirect:/";
		}
		final AsyncExpert<String> expert = (AsyncExpert) context.getExpert();

		action.setContext(context);
		action.setQuestion(expert.getQuestion());

		expert.addAction(action);

		final List<Query<String>> queries = expert.getQueries();
		if (!ValidationUtils.present(queries)) return REDIRECT_IMPLICATIONS;
		
		setQueries(session, queries);

		return REDIRECT_EXPLORATION;
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value="/exploration/reject", method = RequestMethod.POST)
	public String rejectImplication(final HttpSession session) throws InterruptedException {

		final QuestionRejectedAction<String, String, FullObject<String,String>> action = new QuestionRejectedAction<String, String, FullObject<String,String>>();

		final FormalContext<String, String> context = (FormalContext) session.getAttribute("context");
		if (context == null) {
			return "redirect:/";
		}
		final AsyncExpert<String> expert = (AsyncExpert) context.getExpert();

		action.setContext(context);
		action.setQuestion(expert.getQuestion());

		expert.addAction(action);

		final List<Query<String>> queries = expert.getQueries();
		if (!ValidationUtils.present(queries)) return REDIRECT_IMPLICATIONS;
		
		setQueries(session, queries);

		return REDIRECT_EXPLORATION;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/exploration/counterexample", method = RequestMethod.POST)
	public String provideCounterExample(@RequestParam("id") final String id, @RequestParam("attributes") final Set<String> attributes, final HttpSession session) throws InterruptedException {

		final FormalContext<String, String> context = (FormalContext) session.getAttribute("context");
		if (context == null) {
			return "redirect:/";
		}

		final FullObject<String,String> counterExample = new FullObject<String, String>(id);
		counterExample.getDescription().addAttributes(attributes);

		final AsyncExpert<String> expert = (AsyncExpert) context.getExpert();

		final CounterExampleProvidedAction<String, String, FullObject<String,String>> action = new CounterExampleProvidedAction<String, String, FullObject<String,String>>(context, expert.getQuestion(), counterExample);

		expert.addAction(action);

		final List<Query<String>> queries = expert.getQueries();
		if (!ValidationUtils.present(queries)) return REDIRECT_IMPLICATIONS;
		
		setQueries(session, queries);

		return REDIRECT_EXPLORATION;
	}


	public SearchEngine<String> getSearchEngine() {
		return searchEngine;
	}


	public void setSearchEngine(SearchEngine<String> searchEngine) {
		this.searchEngine = searchEngine;
	}
}
