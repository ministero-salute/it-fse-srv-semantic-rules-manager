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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.validators.schematron;

import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.io.resource.inmemory.ReadableResourceByteArray;
import com.helger.schematron.xslt.SchematronResourceSCH;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.SchematronValidatorException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants.Logs.ERR_VAL_INVALID_SCHEMATRON_IO;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchematronValidator {
    public static void verify(byte[] file) throws SchematronValidatorException {
        // Read resource as byte-array stream
        IReadableResource resource = new ReadableResourceByteArray(file);
        // Import resource
        SchematronResourceSCH schematron = new SchematronResourceSCH(resource);
        // Disable cache (it may cause false flag)
        schematron.setUseCache(false);
        // Validate
        if(!schematron.isValidSchematron()) {
            throw new SchematronValidatorException(ERR_VAL_INVALID_SCHEMATRON_IO);
        }
    }

    public static void verify(MultipartFile file) throws SchematronValidatorException {
        try {
            verify(file.getBytes());
        } catch (IOException e) {
            throw new SchematronValidatorException(ERR_VAL_INVALID_SCHEMATRON_IO, e);
        }
    }
}
