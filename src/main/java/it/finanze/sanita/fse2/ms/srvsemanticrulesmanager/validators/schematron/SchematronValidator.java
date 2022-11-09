/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.validators.schematron;

import com.helger.commons.io.resource.IReadableResource;
import com.helger.commons.io.resource.inmemory.ReadableResourceByteArray;
import com.helger.schematron.xslt.SchematronResourceSCH;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.SchematronValidatorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants.Logs.ERR_VAL_INVALID_SCHEMATRON_IO;

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
