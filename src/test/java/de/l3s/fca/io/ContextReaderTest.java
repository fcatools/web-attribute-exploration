package de.l3s.fca.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import de.tudresden.inf.tcs.fcalib.FormalContext;

/**
 * @author rja
 * @version $Id:$
 */
public class ContextReaderTest {

	@Test
	public void testGetFormalContextCEX() throws IOException {

		final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("contexts/triangles.cex");

		assertNotNull(stream);

		final ContextReader contextReader = new ContextReader();
		final FormalContext<String, String> context = contextReader.getFormalContext(stream, ContextReader.FileFormat.CEX);

		assertEquals(5, context.getAttributeCount());
		assertEquals(7, context.getObjectCount());
		assertEquals("T1", context.getObjectAtIndex(0).getIdentifier());
		assertEquals("T2", context.getObjectAtIndex(1).getIdentifier());
		assertEquals("T3", context.getObjectAtIndex(2).getIdentifier());
		assertEquals("T4", context.getObjectAtIndex(3).getIdentifier());
		assertEquals("T5", context.getObjectAtIndex(4).getIdentifier());
		assertEquals("T6", context.getObjectAtIndex(5).getIdentifier());
		assertEquals("T7", context.getObjectAtIndex(6).getIdentifier());

	}
	
	@Test
	public void testGetFormalContextCEX2() throws IOException {

		final InputStream stream = this.getClass().getClassLoader().getResourceAsStream("contexts/europe.cex");

		assertNotNull(stream);

		final ContextReader contextReader = new ContextReader();
		final FormalContext<String, String> context = contextReader.getFormalContext(stream, ContextReader.FileFormat.CEX);

		assertEquals(4, context.getAttributeCount());
		assertEquals(3, context.getObjectCount());
		assertEquals("Czech Republic", context.getObjectAtIndex(0).getIdentifier());
		assertEquals("Norway", context.getObjectAtIndex(1).getIdentifier());
		assertEquals("Germany", context.getObjectAtIndex(2).getIdentifier());
	}

}
