package com.nic.service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestConfiguration
public class MappingConfiguration {

	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
		return mapper;
	}

	@Bean
	MappingJackson2XmlHttpMessageConverter xmlConverter() {
		Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.xml();
		builder.indentOutput(true);
		return new MappingJackson2XmlHttpMessageConverter(builder.build());
	}

}
