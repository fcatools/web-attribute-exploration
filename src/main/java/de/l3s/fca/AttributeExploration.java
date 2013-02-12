package de.l3s.fca;

import de.l3s.fca.web.Google;
import de.tudresden.inf.tcs.fcaapi.Expert;
import de.tudresden.inf.tcs.fcaapi.exception.IllegalObjectException;
import de.tudresden.inf.tcs.fcalib.FormalContext;
import de.tudresden.inf.tcs.fcalib.FullObject;
import de.tudresden.inf.tcs.fcalib.action.StartExplorationAction;

/**
 * 
 * @author:  rja
 * @version: $Id: $
 * $Author: $
 * 
 */
public class AttributeExploration {

	public static void main(String[] args) throws IllegalObjectException {
		// Create a formal context whose attributes are of type String, and whose objects have
		// identifiers of type String
		final FormalContext<String,String> context = new FormalContext<String,String>();

		final Google<String> google = new Google<String>();
		
		// Create an expert for this context
		final WebSearchExpert<String> expert = new WebSearchExpert<String>(context, google);

		expert.setDomain("european countries");
		expert.setSite("en.wikipedia.org");
		
		// Add attributes to this context
		context.addAttribute("NATO");
		context.addAttribute("EU");
		context.addAttribute("EURO");
		context.addAttribute("Schengen");
		
		// Add some objects
		context.addObject(new FullObject<String, String>("czech republic"));
		context.addAttributeToObject("NATO", "czech republic");
		context.addAttributeToObject("EU", "czech republic");
		context.addAttributeToObject("Schengen", "czech republic");
		context.addObject(new FullObject<String, String>("norway"));
		context.addAttributeToObject("NATO", "norway");
		context.addAttributeToObject("Schengen", "norway");
		context.addObject(new FullObject<String, String>("germany"));
		context.addAttributeToObject("NATO", "germany");
		context.addAttributeToObject("EU", "germany");
		context.addAttributeToObject("EURO", "germany");
		context.addAttributeToObject("Schengen", "germany");

		// Set expert for this context
		context.setExpert((Expert) expert);
		// Context listens to the actions of the expert
		expert.addExpertActionListener(context);

		// Create an expert action for starting attribute exploration           
		final StartExplorationAction<String,String,FullObject<String,String>> action = 
		  new StartExplorationAction<String,String,FullObject<String,String>>();
		action.setContext(context);
		// Fire the action, exploration starts...
		expert.fireExpertAction(action);
	}
	
}
