/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.l3s.fca.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jens Illig
 * @version $Id: ValidationUtils.java,v 1.17 2011-08-23 12:26:52 rja Exp $
 */
public class ValidationUtils {

	/**
	 * @param string argument to check
	 * @return false iff the argument is null or has zero trimmed length
	 */
	public static boolean present(final String string) {
		return ((string != null) && (string.trim().length() > 0));
	}
	
	/**
	 * @param charSequence argument to check
	 * @return false iff the argument is null or has zero trimmed length
	 */
	public static boolean present(final CharSequence charSequence) {
		return ((charSequence != null) && (charSequence.length() > 0));
	}
	

	/**
	 * @param collection argument to check
	 * @return false iff the argument is null or has zero size
	 */
	public static boolean present(final Collection<?> collection) {
		return ((collection != null) && (collection.size() > 0));
	}
	
	/**
	 * @param map argument to check
	 * @return false iff the argument is null or has zero size
	 */
	public static boolean present(final Map<?,?> map) {
		return ((map != null) && (map.size() > 0));
	}

	/**
	 * @param object argument to check
	 * @return false iff the argument is null
	 */
	public static boolean present(final Object object) {
		return (object != null);
	}
	
	/**
	 * @param objects array to check
	 * @return false iff the argument is null or has zero length
	 */
	public static boolean present(final Object[] objects) {
		return (objects != null) && objects.length > 0;
	}

	/**
	 * @param requested argument to check
	 * @param supported reference argument for comparison
	 * @return true if <code>requested</code> is null or equals to one of the following arguments
	 */
	public static boolean nullOrEqual(final Object requested, final Object... supported) {
		if (requested == null) return true;
		for (final Object support : supported) {
			if (requested == support) return true;
		}
		return false;
	}
}