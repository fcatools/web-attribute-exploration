package de.l3s.fca.util;

import java.util.Set;

import de.tudresden.inf.tcs.fcaapi.utils.IndexedSet;
import de.tudresden.inf.tcs.fcalib.FormalContext;
import de.tudresden.inf.tcs.fcalib.FullObject;

/**
 * @author rja
 * @version $Id:$
 */
public class FCAUtil {

	private static final int COLUMN_WIDTH = 7;

	public static String toString(final FormalContext<?,?> context) {
		final StringBuilder buf = new StringBuilder();
		
		final IndexedSet<?> attributes = context.getAttributes();
		for (final Object attribute : attributes) {
			buf.append("\t" + StringUtil.shorten(attribute.toString(), 7));
		}
		buf.append("\n");

		final IndexedSet<?> objects = context.getObjects();
		for (final Object object : objects) {
			if (object instanceof FullObject) {
				final FullObject fullObject = (FullObject) object;
				buf.append(StringUtil.shorten(fullObject.getIdentifier().toString(), COLUMN_WIDTH));
				final Set objectAttributes = fullObject.getDescription().getAttributes();
				for (final Object attribute : attributes) {
					buf.append("\t");
					if (objectAttributes.contains(attribute)) {
						buf.append("x");
					}
				}
			} else {
				buf.append("-----");
			}
			buf.append("\n");
		}
		return buf.toString();
	}
	
}
