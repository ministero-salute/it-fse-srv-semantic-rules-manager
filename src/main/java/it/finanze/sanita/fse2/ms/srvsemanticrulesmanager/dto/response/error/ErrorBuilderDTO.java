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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error;


import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.log.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error.ErrorInstance.Resource;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error.ErrorInstance.Server;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error.ErrorInstance.Validation;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.exceptions.*;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.utility.UtilsMisc;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants.Logs.ERR_VAL_UNABLE_CONVERT;
import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error.ErrorInstance.IO;
import static org.apache.http.HttpStatus.*;

public final class ErrorBuilderDTO {

    /**
     * Private constructor to disallow to access from other classes
     */
    private ErrorBuilderDTO() {}

    public static ErrorResponseDTO createConstraintError(LogTraceInfoDTO trace, ConstraintViolationException ex) {
        // Retrieve the first constraint error
        ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();
        String field = UtilsMisc.extractKeyFromPath(violation.getPropertyPath());
        // Return associated information
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            violation.getMessage(),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, field)
        );
    }


    public static ErrorResponseDTO createGenericError(LogTraceInfoDTO trace, Exception ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.SERVER.getType(),
            ErrorType.SERVER.getTitle(),
            ex.getMessage(),
            SC_INTERNAL_SERVER_ERROR,
            ErrorType.SERVER.toInstance(Server.INTERNAL)
        );
    }

    public static ErrorResponseDTO createInvalidVersionError(LogTraceInfoDTO trace, InvalidVersionException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            ex.getMessage(),
            SC_CONFLICT,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, "version")
        );

    }


    public static ErrorResponseDTO createOperationError(LogTraceInfoDTO trace, OperationException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.SERVER.getType(),
            ErrorType.SERVER.getTitle(),
            ex.getMessage(),
            SC_INTERNAL_SERVER_ERROR,
            ErrorType.SERVER.toInstance(Server.INTERNAL)
        );
    }

    public static ErrorResponseDTO createDocumentNotFoundError(LogTraceInfoDTO trace, DocumentNotFoundException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.RESOURCE.getType(),
            ErrorType.RESOURCE.getTitle(),
            ex.getMessage(),
            SC_NOT_FOUND,
            ErrorType.RESOURCE.toInstance(Resource.NOT_FOUND)
        );
    }

    public static ErrorResponseDTO createSchematronValidatorException(LogTraceInfoDTO trace, SchematronValidatorException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.IO.getType(),
            ErrorType.IO.getTitle(),
            ex.getMessage(),
            SC_UNPROCESSABLE_ENTITY,
            ErrorType.IO.toInstance(IO.CONVERSION)
        );
    }

    public static ErrorResponseDTO createArgumentMismatchError(LogTraceInfoDTO trace, MethodArgumentTypeMismatchException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            String.format(
                ERR_VAL_UNABLE_CONVERT,
                ex.getName(),
                ex.getParameter().getParameter().getType().getSimpleName()
            ),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(ErrorInstance.Validation.CONSTRAINT_FIELD, ex.getName())
        );
    }

    public static ErrorResponseDTO createDocumentAlreadyPresentError(LogTraceInfoDTO trace, DocumentAlreadyPresentException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.RESOURCE.getType(),
            ErrorType.RESOURCE.getTitle(),
            ex.getMessage(),
            SC_CONFLICT,
            ErrorType.RESOURCE.toInstance(Resource.CONFLICT)
        );
    } 

    public static ErrorResponseDTO createInvalidContentError(LogTraceInfoDTO trace, InvalidContentException ex) {
        return new ErrorResponseDTO(
            trace,
            ErrorType.VALIDATION.getType(),
            ErrorType.VALIDATION.getTitle(),
            ex.getMessage(),
            SC_BAD_REQUEST,
            ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, "file")
        );
    }

    public static ErrorResponseDTO createMissingPartError(LogTraceInfoDTO trace, MissingServletRequestPartException ex) {
        return new ErrorResponseDTO(
                trace,
                ErrorType.VALIDATION.getType(),
                ErrorType.VALIDATION.getTitle(),
                ex.getMessage(),
                SC_BAD_REQUEST,
                ErrorType.VALIDATION.toInstance(Validation.CONSTRAINT_FIELD, ex.getRequestPartName())
        );
    }

}
