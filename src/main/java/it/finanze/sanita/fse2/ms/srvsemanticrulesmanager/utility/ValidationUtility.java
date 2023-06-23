/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility;

public class ValidationUtility {
	
	private ValidationUtility() {
		
	}
	
	public static final int DEFAULT_STRING_MIN_SIZE = 0;
	public static final int DEFAULT_STRING_MAX_SIZE = 100;
	public static final int DEFAULT_NUMBER_MIN_SIZE = 0;
	public static final int DEFAULT_NUMBER_MAX_SIZE = 10000;
	public static final int DEFAULT_ARRAY_MIN_SIZE = 0;
	public static final int DEFAULT_ARRAY_MAX_SIZE = 10000;
	public static final int DEFAULT_BINARY_MIN_SIZE = 0;
	public static final int DEFAULT_BINARY_MAX_SIZE = 10000;

	public static boolean isMajorVersion(final String newVersion, final String lastVersion) {
		boolean isMajor = false;
		String[] lastVersionChunks = lastVersion.split("\\.");
		String[] newVersionChunks = newVersion.split("\\.");

		int lastVersionMajor = Integer.parseInt(lastVersionChunks[0]);
		int newVersionMajor = Integer.parseInt(newVersionChunks[0]);
		int lastVersionMinor = Integer.parseInt(lastVersionChunks[1]);
		int newVersionMinor = Integer.parseInt(newVersionChunks[1]);

		if (lastVersionMajor < newVersionMajor) {
			isMajor = true;
		} else if (lastVersionMajor == newVersionMajor) {
			isMajor = lastVersionMinor < newVersionMinor;
		}
		
		return isMajor;
	}	
}
