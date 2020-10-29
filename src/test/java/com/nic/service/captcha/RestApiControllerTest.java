package com.nic.service.captcha;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest({ RestApiController.class })
class RestApiControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper mapper = new ObjectMapper();

	@Test
	void testGenerateCaptcha_ContentNotNullAndOfPNGType() throws Exception {
		MvcResult result = mvc.perform(get("/api/captcha/generate")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_PNG)).andReturn();

		assertThat(result.getResponse().getContentAsByteArray()).isNotNull();
	}

	@Test
	@Disabled("Colors are different, hence won't match. When colors are same, then should match")
	void testGenerateCaptcha_ByteArrayContentExactMatch() throws Exception {
		MockHttpSession session = new MockHttpSession();

		MvcResult result = mvc.perform(get("/api/captcha/generate").session(session)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_PNG)).andReturn();

		String captchaText = (String) session.getAttribute(RestApiController.sessionHolder);
		assertThat(Captcha.generateImage(captchaText)).isEqualTo(result.getResponse().getContentAsByteArray());
	}

	@Test
	void testGenerateCaptcha_CaptchaTextStoredInSession() throws Exception {
		MockHttpSession session = new MockHttpSession();

		mvc.perform(get("/api/captcha/generate").session(session)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.IMAGE_PNG));

		assertThat((String) session.getAttribute(RestApiController.sessionHolder)).isNotBlank();
	}

	@Test
	void testValidateCaptcha_CaptchaIsValid() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(RestApiController.sessionHolder, "321654987");

		mvc.perform(post("/api/captcha/validate").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new RestApiController.CaptchaValue("321654987"))).session(session))
				.andExpect(status().isOk()).andExpect(handler().methodName("validateCaptcha"))
				.andExpect(content().string("Captcha is valid"));
	}

	@Test
	void testValidateCaptcha_CaptchaNotIsValid() throws Exception {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(RestApiController.sessionHolder, "321654987");

		mvc.perform(post("/api/captcha/validate").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new RestApiController.CaptchaValue("981265477"))).session(session))
				.andExpect(status().isBadRequest()).andExpect(handler().methodName("validateCaptcha"))
				.andExpect(jsonPath("$.errors", hasItem("Captcha is not valid")));
	}

	@Test
	void testValidateCaptcha_NoCaptchaProvidedInRequestBody() throws Exception {
		mvc.perform(post("/api/captcha/validate").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new RestApiController.CaptchaValue(null))))
				.andExpect(status().isBadRequest()).andExpect(handler().methodName("validateCaptcha"))
				.andExpect(jsonPath("$.errors", hasItem("Please provide captcha value")));
	}

	@Test
	void testValidateCaptcha_EmptyCaptchaProvidedInRequestBody() throws Exception {
		mvc.perform(post("/api/captcha/validate").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new RestApiController.CaptchaValue(""))))
				.andExpect(status().isBadRequest()).andExpect(handler().methodName("validateCaptcha"))
				.andExpect(jsonPath("$.errors", hasItem("Please provide captcha value")));
	}

	@Test
	void testValidateCaptcha_CaptchaIsValid_FullFlow() throws Exception {
		MockHttpSession session = new MockHttpSession();

		mvc.perform(get("/api/captcha/generate").session(session)).andExpect(status().isOk());

		String captchaTest = (String) session.getAttribute(RestApiController.sessionHolder);
		assertThat(captchaTest).isNotNull();

		mvc.perform(post("/api/captcha/validate").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new RestApiController.CaptchaValue(captchaTest))).session(session))
				.andExpect(status().isOk());
	}

}
