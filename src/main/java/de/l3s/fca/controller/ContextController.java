package de.l3s.fca.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import de.l3s.fca.io.ContextReader;
import de.l3s.fca.io.ContextReader.FileFormat;
import de.l3s.fca.util.FileUtil;
import de.tudresden.inf.tcs.fcalib.FormalContext;

/**
 * @author rja
 * @version $Id:$
 */
@Controller
public class ContextController {

	private static final Map<String, String> CONTEXTS = new HashMap<String, String>();
	static {
		CONTEXTS.put("triangles", "contexts/triangles.cex");
		CONTEXTS.put("europe", "contexts/europe.cex");
		CONTEXTS.put("national_parks", "contexts/national_parks.cex");
		CONTEXTS.put("praesidenten", "contexts/praesidenten.cex");
	}

	@RequestMapping(value="/context", method = RequestMethod.GET)
	public String selectContext(
			@RequestParam("contextId") final String contextId,
			@RequestParam(value="site", required=false, defaultValue="") final String site,
			@RequestParam(value="domain", required=false, defaultValue="") final String domain,
			final HttpSession session) throws IOException {
		/*
		 * load context into session
		 */
		if (contextId == null || !CONTEXTS.containsKey(contextId)) {
//			result.reject("Could not find context " + contextId);
			return "index.jspx";
		}
		
		session.setAttribute("context", getContext(contextId));
		session.setAttribute("domain", domain);
		session.setAttribute("site", site);
		return "redirect:/exploration";
	}

	@RequestMapping(value="/context", method = RequestMethod.POST)
	public String uploadContext(
			@RequestParam("contextFile") final MultipartFile contextFile, 
			@RequestParam(value="site", required=false, defaultValue="") final String site,
			@RequestParam(value="domain", required=false, defaultValue="") final String domain,			
			final HttpSession session) throws IOException {
		if (!contextFile.isEmpty()) {
			session.setAttribute("context", getContext(contextFile.getInputStream(), contextFile.getOriginalFilename()));
			session.setAttribute("domain", domain);
			session.setAttribute("site", site);
			return "redirect:/exploration";
		} 
		return "redirect:/";
	}


	private FormalContext<String, String> getContext(final String contextId) throws IOException {
		final String fileName = CONTEXTS.get(contextId);
		final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileName);
		return getContext(stream, fileName);
	}

	private FormalContext<String, String> getContext(final InputStream stream, final String fileName) throws IOException {
		final FileFormat fileFormat = ContextReader.FileFormat.getByExtension(FileUtil.getExtension(fileName));
		return new ContextReader().getFormalContext(stream, fileFormat);
	}

}
