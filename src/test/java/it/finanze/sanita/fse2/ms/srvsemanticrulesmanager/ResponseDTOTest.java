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
package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.changes.ChangeSetResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.crud.DelDocsResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.crud.PostDocsResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.crud.PutDocsResDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.error.base.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.log.LogTraceInfoDTO;

class ResponseDTOTest extends AbstractTest {
	
	public String SPAN_ID_TEST = "d9fg5hkaq8"; 
	public String TRACE_ID_TEST = "d9fgd8aasd"; 
	
	public String IN_TYPE_TEST = "type"; 
	public String IN_TITLE_TEST = "title"; 
	public String IN_DETAIL_TEST = "detail"; 
	public Integer IN_STATUS_TEST = 1; 
	public String IN_INSTANCE_TEST = "instance";
	
	@Test
	void logTraceInfoDtoTest() {
		LogTraceInfoDTO dto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 
		
		assertEquals(LogTraceInfoDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
		
	} 
	
	@Test
	void responseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		ResponseDTO dto = new ResponseDTO(logTraceInfoDto); 
		
		assertEquals(ResponseDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
	} 
	
	@Test
	void errorResponseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		ErrorResponseDTO dto = new ErrorResponseDTO(logTraceInfoDto, IN_TYPE_TEST, IN_TITLE_TEST,
				IN_DETAIL_TEST, IN_STATUS_TEST, IN_INSTANCE_TEST); 
		
		
		assertEquals(ErrorResponseDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getType().getClass()); 
		assertEquals(String.class, dto.getTitle().getClass()); 
		assertEquals(String.class, dto.getDetail().getClass()); 
		assertEquals(Integer.class, dto.getStatus().getClass()); 
		assertEquals(String.class, dto.getInstance().getClass()); 
		
		assertEquals(IN_TYPE_TEST, dto.getType()); 
		assertEquals(IN_TITLE_TEST, dto.getTitle()); 
		assertEquals(IN_DETAIL_TEST, dto.getDetail()); 
		assertEquals(IN_STATUS_TEST, dto.getStatus()); 
		assertEquals(IN_INSTANCE_TEST, dto.getInstance()); 
		
	} 
	
	@Test
	void errorResponseDtoLogInfoOnlyTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		ErrorResponseDTO dto = new ErrorResponseDTO(logTraceInfoDto); 
		
		
		assertEquals(ErrorResponseDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
	}
	
	@Test
	void postResponseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		PostDocsResDTO dto = new PostDocsResDTO(logTraceInfoDto, 1);
		
		assertEquals(PostDocsResDTO.class, dto.getClass());
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
		
	}
	
	@Test
	void putResponseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		PutDocsResDTO dto = new PutDocsResDTO(logTraceInfoDto, 1);
		
		assertEquals(PutDocsResDTO.class, dto.getClass());
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
		
	}
	
	@Test
	void deleteResponseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		DelDocsResDTO dto = new DelDocsResDTO(logTraceInfoDto, 1);
		
		assertEquals(DelDocsResDTO.class, dto.getClass());
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
		
	}
	
	@Test
	void changesetResponseDtoTest() {
		ChangeSetResDTO changesetResponse = new ChangeSetResDTO();
		
		Date date = new Date(); 
		
		changesetResponse.setInsertions(new ArrayList<>());
		changesetResponse.setDeletions(new ArrayList<>());
		changesetResponse.setTimestamp(date); 
		changesetResponse.setLastUpdate(date); 
		changesetResponse.setTotalNumberOfElements(1); 
		
		assertEquals(ArrayList.class, changesetResponse.getInsertions().getClass()); 
		assertEquals(ArrayList.class, changesetResponse.getDeletions().getClass()); 
		assertEquals(Date.class, changesetResponse.getTimestamp().getClass()); 
		assertEquals(Date.class, changesetResponse.getLastUpdate().getClass()); 

		assertEquals(date, changesetResponse.getTimestamp()); 
		assertEquals(date, changesetResponse.getLastUpdate()); 
		assertEquals(1, changesetResponse.getTotalNumberOfElements()); 

	} 
	
	@Test
	void changesetResponseDtoInitTest() {
		LogTraceInfoDTO logTraceInfo = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 
		ChangeSetResDTO changesetResponse = new ChangeSetResDTO(
			logTraceInfo.getTraceID(), logTraceInfo.getSpanID(), new Date(), new Date(),
			new ArrayList<>(), new ArrayList<>(), 1, 1);
		
		assertEquals(ArrayList.class, changesetResponse.getInsertions().getClass()); 
		assertEquals(ArrayList.class, changesetResponse.getDeletions().getClass()); 
		assertEquals(Date.class, changesetResponse.getTimestamp().getClass()); 
		assertEquals(Date.class, changesetResponse.getLastUpdate().getClass()); 

		assertEquals(1, changesetResponse.getTotalNumberOfElements()); 

	}
	
}
