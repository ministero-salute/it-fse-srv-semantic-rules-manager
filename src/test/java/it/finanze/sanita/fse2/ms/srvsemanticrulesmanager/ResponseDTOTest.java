package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.ChangeSetDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ChangeSetResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.LogTraceInfoDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.ResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronCreationErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronErrorResponseDTO;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.dto.response.SchematronResponseDTO;

class ResponseDTOTest extends AbstractTest {

	public HttpStatus RESPONSE_CODE_TEST = HttpStatus.OK; 
	public String RESPONSE_MESSAGE_TEST = "Test Message"; 
	
	public String SPAN_ID_TEST = "d9fg5hkaq8"; 
	public String TRACE_ID_TEST = "d9fgd8aasd"; 
	
	public String IN_TYPE_TEST = "type"; 
	public String IN_TITLE_TEST = "title"; 
	public String IN_DETAIL_TEST = "detail"; 
	public Integer IN_STATUS_TEST = 1; 
	public String IN_INSTANCE_TEST = "instance"; 
	public String IN_TX_ID_TEST = "txId"; 

	
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
	void schematronResponseDtoNoArgsTest() {

		SchematronResponseDTO dto = new SchematronResponseDTO(); 
		
		assertEquals(SchematronResponseDTO.class, dto.getClass()); 

		
	}
	
	@Test
	void schematronResponseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		SchematronResponseDTO dto = new SchematronResponseDTO(logTraceInfoDto, IN_TX_ID_TEST); 
		
		assertEquals(SchematronResponseDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getSpanID().getClass()); 
		assertEquals(String.class, dto.getTraceID().getClass()); 
		assertEquals(String.class, dto.getTransactionId().getClass());  
		
		assertEquals(SPAN_ID_TEST, dto.getSpanID()); 
		assertEquals(TRACE_ID_TEST, dto.getTraceID()); 
		assertEquals(IN_TX_ID_TEST, dto.getTransactionId()); 
		
	}
	
	@Test
	void schematronErrorResponseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		SchematronErrorResponseDTO dto = new SchematronErrorResponseDTO(logTraceInfoDto, IN_TYPE_TEST, IN_TITLE_TEST,
				IN_DETAIL_TEST, IN_STATUS_TEST, IN_INSTANCE_TEST, IN_TX_ID_TEST); 
		
		
		assertEquals(SchematronErrorResponseDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getType().getClass()); 
		assertEquals(String.class, dto.getTitle().getClass()); 
		assertEquals(String.class, dto.getDetail().getClass()); 
		assertEquals(Integer.class, dto.getStatus().getClass()); 
		assertEquals(String.class, dto.getInstance().getClass()); 
		assertEquals(String.class, dto.getTransactionId().getClass()); 
		
		assertEquals(IN_TYPE_TEST, dto.getType()); 
		assertEquals(IN_TITLE_TEST, dto.getTitle()); 
		assertEquals(IN_DETAIL_TEST, dto.getDetail()); 
		assertEquals(IN_STATUS_TEST, dto.getStatus()); 
		assertEquals(IN_INSTANCE_TEST, dto.getInstance()); 
		assertEquals(IN_TX_ID_TEST, dto.getTransactionId()); 
	}
	
	@Test
	void schematronCreationErrorResponseDtoTest() {
		LogTraceInfoDTO logTraceInfoDto = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 

		SchematronCreationErrorResponseDTO dto = new SchematronCreationErrorResponseDTO(logTraceInfoDto, IN_TYPE_TEST, IN_TITLE_TEST,
				IN_DETAIL_TEST, IN_STATUS_TEST, IN_INSTANCE_TEST, IN_TX_ID_TEST); 
		
		
		assertEquals(SchematronCreationErrorResponseDTO.class, dto.getClass()); 
		assertEquals(String.class, dto.getType().getClass()); 
		assertEquals(String.class, dto.getTitle().getClass()); 
		assertEquals(String.class, dto.getDetail().getClass()); 
		assertEquals(Integer.class, dto.getStatus().getClass()); 
		assertEquals(String.class, dto.getInstance().getClass()); 
		assertEquals(String.class, dto.getTransactionId().getClass()); 
		
		assertEquals(IN_TYPE_TEST, dto.getType()); 
		assertEquals(IN_TITLE_TEST, dto.getTitle()); 
		assertEquals(IN_DETAIL_TEST, dto.getDetail()); 
		assertEquals(IN_STATUS_TEST, dto.getStatus()); 
		assertEquals(IN_INSTANCE_TEST, dto.getInstance()); 
		assertEquals(IN_TX_ID_TEST, dto.getTransactionId()); 
	} 
	
	@Test
	void changesetResponseDtoTest() {
		ChangeSetResponseDTO changesetResponse = new ChangeSetResponseDTO(); 
		
		Date date = new Date(); 
		
		changesetResponse.setInsertions(new ArrayList<ChangeSetDTO>()); 
		changesetResponse.setDeletions(new ArrayList<ChangeSetDTO>()); 
		changesetResponse.setModifications(new ArrayList<ChangeSetDTO>()); 
		changesetResponse.setTimestamp(date); 
		changesetResponse.setLastUpdate(date); 
		changesetResponse.setTotalNumberOfElements(1); 
		
		assertEquals(ArrayList.class, changesetResponse.getInsertions().getClass()); 
		assertEquals(ArrayList.class, changesetResponse.getDeletions().getClass()); 
		assertEquals(ArrayList.class, changesetResponse.getModifications().getClass()); 
		assertEquals(Date.class, changesetResponse.getTimestamp().getClass()); 
		assertEquals(Date.class, changesetResponse.getLastUpdate().getClass()); 

		assertEquals(date, changesetResponse.getTimestamp()); 
		assertEquals(date, changesetResponse.getLastUpdate()); 
		assertEquals(1, changesetResponse.getTotalNumberOfElements()); 

	} 
	
	@Test
	void changesetResponseDtoInitTest() {
		LogTraceInfoDTO logTraceInfo = new LogTraceInfoDTO(SPAN_ID_TEST, TRACE_ID_TEST); 
		ChangeSetResponseDTO changesetResponse = new ChangeSetResponseDTO(logTraceInfo, "txId", new Date(), new Date(), 
				new ArrayList<ChangeSetDTO>(), new ArrayList<ChangeSetDTO>(), new ArrayList<ChangeSetDTO>(), 1); 
		
		assertEquals(ArrayList.class, changesetResponse.getInsertions().getClass()); 
		assertEquals(ArrayList.class, changesetResponse.getDeletions().getClass()); 
		assertEquals(ArrayList.class, changesetResponse.getModifications().getClass()); 
		assertEquals(Date.class, changesetResponse.getTimestamp().getClass()); 
		assertEquals(Date.class, changesetResponse.getLastUpdate().getClass()); 

		assertEquals(1, changesetResponse.getTotalNumberOfElements()); 

	}
	
}
