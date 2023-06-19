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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import javax.validation.Path;

public final class UtilsMisc {

    /**
     * Private constructor to disallow to access from other classes
     */
    private UtilsMisc() {}

    public static OffsetDateTime convertToOffsetDateTime(Date dateToConvert) {
        return dateToConvert.toInstant().atOffset(ZoneOffset.UTC);
    }
    /**
     * Encode in Base64 the byte array passed as parameter.
     *
     * @param input	The byte array to encode.
     * @return		The encoded byte array to String.
     */
    public static String encodeBase64(final byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static String extractKeyFromPath(Path path) {
        String field = "";
        for(Path.Node node: path) field = node.getName();
        return field;
    }
}
