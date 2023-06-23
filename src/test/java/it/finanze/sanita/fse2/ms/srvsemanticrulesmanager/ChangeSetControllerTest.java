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

import static it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.MockRequests.getSchematronChangeSet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import brave.Tracer;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.base.AbstractTest;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.config.Constants;
import it.finanze.sanita.fse2.ms.srvsemanticrulesmanager.controller.impl.SchematronCTL;

@AutoConfigureMockMvc
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
