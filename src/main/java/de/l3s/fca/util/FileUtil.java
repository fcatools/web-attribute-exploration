package de.l3s.fca.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * @author rja
 * @version $Id:$
 */
public class FileUtil {

	/**
	 * Appends a given extension to a fileName unless the filename already ends with that extension
	 * @param fileName
	 * @param extension
	 * @return
	 */
	public static String getFileName(final String fileName, final String extension) {
		return fileName + (fileName.endsWith(extension) ? "" : extension);		
	}
	
	/**
	 * Get a BufferedWriter for a file of the given fileName
	 */
	public static BufferedWriter getWriter(final String fileName) throws UnsupportedEncodingException, FileNotFoundException {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
	}
	
	public static StringBuilder getContent(final String fileName) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		return getContent(FileUtil.getReader(fileName));
	}
	
	public static StringBuilder getContent(final InputStream stream) throws UnsupportedEncodingException, IOException {
		return getContent(FileUtil.getReader(stream));
	}

	/**
	 * TODO: read bytes/use StringWriter
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static StringBuilder getContent(final Reader reader) throws IOException {
		final BufferedReader bufferedReader = new BufferedReader(reader);
		final StringBuilder buf = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			buf.append(line).append("\n");
		}
		return buf;
	}
	
	/**
	 * Get a BufferedReader for the file of the given fileName
	 * @param fileName
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static BufferedReader getReader(final String fileName) throws UnsupportedEncodingException, FileNotFoundException {
		return getReader(new FileInputStream(fileName));
	}

	public static BufferedReader getReader(final InputStream in) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(in, "UTF-8"));
	}
	
	/**
	 * Get a BufferedWriter for the file with the given fileName and Extension
	 * @param fileName
	 * @param extension
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static BufferedWriter getWriter(final String fileName, final String extension) throws UnsupportedEncodingException, FileNotFoundException {
		return getWriter(getFileName(fileName, extension));
	}
	
	/**
	 * Removes the extension from the filename = skips everything after the last
	 * dot ".".
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getBasename(final String fileName) {
		final int lastIndexOfDot = fileName.lastIndexOf(".");
		if (lastIndexOfDot > 0) {
			return fileName.substring(0, lastIndexOfDot);
		}
		return fileName;
	}
	
	/**
	 * Returns the extension of the filename = skips everything before the last
	 * dot ".".
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtension(final String fileName) {
		final int lastIndexOfDot = fileName.lastIndexOf(".");
		if (lastIndexOfDot > 0) {
			return fileName.substring(lastIndexOfDot + 1);
		}
		return fileName;
	}
}
