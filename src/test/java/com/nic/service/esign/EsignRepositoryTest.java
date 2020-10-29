package com.nic.service.esign;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.nic.service.MappingConfiguration;
import com.nic.service.DataJpaTestWrapper;

// issues with this, .. need to set data for successful e-signed 
@Import({ EsignRepository.class, MappingConfiguration.class })
class EsignRepositoryTest extends DataJpaTestWrapper {

	@Autowired
	private EsignRepository repository;

	@Autowired
	private MappingJackson2XmlHttpMessageConverter xmlConverter;

	private ESignResponseModel response;

	@BeforeEach
	void beforeEach() throws JsonParseException, JsonMappingException, IOException {
		XmlMapper mapper = (XmlMapper) xmlConverter.getObjectMapper();
		File xmlFile = new File(getClass().getResource("esign-response-success.xml").getPath());
		response = mapper.readValue(xmlFile, ESignResponseModel.class);
	}

	@Test
	void testGetEsignTransactionSequenceNoNotNull() {
		assertNotNull(repository.getEsignTransactionSequenceNo());
	}

	@Test
	void testGetEsignTransactionSequenceNoNotZero() {
		assertTrue(repository.getEsignTransactionSequenceNo() != 0);
	}

	@Test
	void testEsignResponseModelMapping() {
		assertTrue(response.getTransactionId().equals("999-BHARATAPI-20200825-164922-000016"));
	}

	@Test
	@Disabled
	void testSaveEsignTransactionDetails() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateEsignTransactionDetails() {
		assertTrue(repository.updateEsignTransactionDetails(response) == 38);
	}

	@Test
	@Disabled
	void testReturnEsignResponse() {
		fail("Not yet implemented");
	}

}
