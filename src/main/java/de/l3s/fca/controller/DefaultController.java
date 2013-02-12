package de.l3s.fca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author rja
 * @version $Id:$
 */
@Controller
public class DefaultController {

	@RequestMapping(value="/")
	public String index(final Model model) {
		return "index.jspx";
	}
}
