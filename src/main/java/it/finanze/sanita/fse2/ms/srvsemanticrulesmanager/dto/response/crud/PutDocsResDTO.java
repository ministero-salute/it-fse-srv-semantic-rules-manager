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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.crud;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.log.LogTraceInfoDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * The Class SchematronCreationResponseDTO.
 *
 * 
 * 	Schematron Response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PutDocsResDTO extends ResponseDTO {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5857199886068379718L;

	private Integer updatedItems;

	public PutDocsResDTO(final LogTraceInfoDTO traceInfo, final Integer updatedItems) {
		super(traceInfo);
		this.updatedItems = updatedItems;
	}
	
}
