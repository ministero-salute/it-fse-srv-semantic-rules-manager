package it.finanze.sanita.fse2.ms.srvsemanticrulesmanager;

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.MockRequests.getSchematronChangeSet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl.SchematronCTL;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@WebMvcTest(SchematronCTL.class)
@ComponentScan
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles(Constants.Profile.TEST)
class ChangeSetControllerTest extends AbstractTest {

	@MockBean
	private Tracer tracer;

		
	@Autowired
	private MockMvc mvc; 

	@Test
	void schematronChangeSet() throws Exception {

		String queryDate = "2022-06-04T12:08:56.000-00:00";

		mvc.perform(getSchematronChangeSet(queryDate)).andExpectAll(
	            status().is2xxSuccessful(),
	            content().contentType(APPLICATION_JSON_VALUE)
	        );

	}

	@Test
	void getFutureStatus() throws Exception {
		// Future date
		String queryDate = "2210-06-04T12:08:56.000-00:00";
		// Execute request
		mvc.perform(
			getSchematronChangeSet(queryDate)
		).andExpectAll(
			status().is4xxClientError(),
			content().contentType(APPLICATION_PROBLEM_JSON)
		);
	}

}
