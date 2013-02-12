package de.l3s.fca.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.l3s.fca.util.FileUtil;
import de.tudresden.inf.tcs.fcalib.FormalContext;

/**
 * @author rja
 * @version $Id:$
 */
public class ContextReader {

	public static enum FileFormat {
		CEX,
		CXT;
		
		public static FileFormat getByExtension(final String s) {
			if ("cex".equals(s)) return CEX;
			if ("cxt".equals(s)) return CXT;
			throw new IllegalArgumentException("Unknown Context File Extension");
		}
	}
	
	public FormalContext<String, String> getFormalContext(final InputStream stream, final FileFormat fileFormat) throws IOException {
		switch (fileFormat) {
		case CEX:
			return getFormalContextCEX(FileUtil.getReader(stream));
		default:
			System.err.println("Unsupported File Format");
			break;
		}
		return null;
	}
	
	
	public FormalContext<String, String> getFormalContextCEX(final Reader reader) throws IOException {
		try  {
			final XMLReader xr = XMLReaderFactory.createXMLReader();
			/*
			 * SAX callback handler
			 */
			final XMLHandler handler = new XMLHandler();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			xr.parse(new InputSource(reader));
			return handler.getFormalContext();
		} catch (SAXException e) {
			throw new IOException(e);
		}
	}
	
	
	
	
}
