package de.l3s.fca.io;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import de.tudresden.inf.tcs.fcaapi.exception.IllegalObjectException;
import de.tudresden.inf.tcs.fcalib.FormalContext;
import de.tudresden.inf.tcs.fcalib.FullObject;


/**
 * Callback handler for the SAX parser, reading EasyChair XML.
 * 
 * 
 * @author rja
 * @version $Id: XMLHandler.java,v 1.1 2011-12-14 14:55:51 rja Exp $
 */
public class XMLHandler extends DefaultHandler {
	private StringBuffer buf = new StringBuffer();

	private FormalContext<String,String> formalContext;

	/*
	 * temporary variables
	 */
	private boolean inAttribute = false;
	private boolean inObject = false;
	private boolean inIntent = false;
	private String objectName;
	private int attributeIdentifier;
	private final Map<Integer, String> attributes = new HashMap<Integer, String>();
	private Set<String> objectAttributes;

	@Override
	public void startDocument() {
	}

	@Override
	public void endDocument() {
	}

	@Override
	public void startElement (final String uri, final String name, final String qName, final Attributes atts) {
		if ("Context".equals(qName)) {
			formalContext = new FormalContext<String, String>();
		} else if ("Attribute".equals(qName)) {
			attributeIdentifier = Integer.parseInt(atts.getValue("Identifier"));
			inAttribute = true;
		} else if ("Object".equals(qName)) {
			inObject = true;
		} else if ("Intent".equals(qName)) {			
			inIntent = true;
			objectAttributes = new HashSet<String>();
		} else if ("HasAttribute".equals(qName) && inIntent) {
			objectAttributes.add(attributes.get(Integer.parseInt(atts.getValue("AttributeIdentifier"))));
		}
		this.buf = new StringBuffer();
	}

	/** Collect characters.
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters (final char ch[], final int start, final int length) {
		buf.append(ch, start, length);
	}

	@Override
	public void endElement (final String uri, final String name, final String qName) {
		if ("Attribute".equals(qName)) {
			inAttribute = false;
		} else if ("Name".equals(qName)) {
			if (inAttribute) {
				// map attribute
				attributes.put(attributeIdentifier, buf.toString());
				formalContext.addAttribute(buf.toString());
			} else if (inObject) {
				objectName = buf.toString() ;
			}
		} else if ("Object".equals(qName)) {
			inObject = false;
			// add object to context
			try {
				formalContext.addObject(new FullObject<String, String>(objectName, objectAttributes));
			} catch (IllegalObjectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public FormalContext<String, String> getFormalContext() {
		return formalContext;
	}

}
