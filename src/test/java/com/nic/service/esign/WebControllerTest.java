package com.nic.service.esign;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.itextpdf.text.DocumentException;
import com.nic.service.utils.Utils;

@WebMvcTest(controllers = { WebController.class }, properties = { "application.domain-name=napix.gov.in",
		"application.sub-directory=onboarding",
		"nic-esign.gateway.main-url=https://nic-esigngateway.nic.in/eSign21/acceptClient",
		"react.application-base-url=http://localhost:3000" })
class WebControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private MappingJackson2XmlHttpMessageConverter xmlConverter;

	@MockBean
	private EsignService service;

	@Value("${application.domain-name}")
	private String domainName;

	@Value("${application.sub-directory}")
	private String subDirectoryName;

	@Value("${nic-esign.gateway.main-url}")
	private String eSignGatewayUrl;

	@Value("${react.application-base-url}")
	private String reactApplicationUrl;

	private XmlMapper xmlMapper;

	@BeforeEach
	public void beforeAll() {
		xmlMapper = (XmlMapper) xmlConverter.getObjectMapper();
	}

	// ------- test for 'receiveEsignGatewayResponse' method

	@Test
	final void testReceiveEsignGatewayResponse_EsignResponse_NotProvided() throws Exception {
		mvc.perform(post("/services/esign/process/gateway-response")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", "No response from e-sign service provided!"));
	}

	@Test
	final void testReceiveEsignGatewayResponse_EsignResponse_InvalidResponse() throws Exception {
		ESignResponseModel response = ESignResponseModelObjectMother.aReponseWithOnlyErrorCode().build();

		when(service.processESignResponse(response)).thenThrow(IllegalArgumentException.class);

		mvc.perform(post("/services/esign/process/gateway-response").param("respon",
				xmlMapper.writeValueAsString(response))).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", "Unknown Exception occured!"))
				.andExpect(model().attributeDoesNotExist("application-no"));
	}

	@Test
	@Disabled("need to overrides equals & hash for comparison purposes")
	final void testReceiveEsignGatewayResponse_EsignResponse_ErrorResponse() throws Exception {
		ESignResponseModel response = ESignResponseModelObjectMother.aErrorResponse().build();

		when(service.processESignResponse(response)).thenReturn("NAPIX/98745");

		mvc.perform(post("/services/esign/process/gateway-response").param("respon",
				xmlMapper.writeValueAsString(response))).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", response.getErrorMessage()))
				.andExpect(model().attribute("application-no", "NAPIX/98745"));
	}

	@Test
	@Disabled("need to overrides equals & hash for comparison purposes")
	final void testReceiveEsignGatewayResponse_EsignResponse_ValidResponse() throws Exception {
		ESignResponseModel response = ESignResponseModelObjectMother.aNonErrorResponse().build();

		when(service.processESignResponse(response)).thenReturn("NAPIX/20012");

		mvc.perform(post("/services/esign/process/gateway-response").param("respon",
				xmlMapper.writeValueAsString(response))).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "success"))
				.andExpect(model().attributeDoesNotExist("error-message"))
				.andExpect(model().attribute("application-no", "NAPIX/20012"));
	}

	@Test
	@Disabled("need to overrides equals & hash for comparison purposes")
	final void testReceiveEsignGatewayResponse_ResponseWithApplicationTypeUrlFormEncodedSucceed() throws Exception {
		ESignResponseModel responseModel = ESignResponseModelObjectMother.aNonErrorResponse().build();
		when(service.processESignResponse(responseModel)).thenReturn("NAPIX/32145");

		mvc.perform(post("/services/esign/process/gateway-response")
				.param("respon", xmlConverter.getObjectMapper().writeValueAsString(responseModel))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)).andExpect(status().is3xxRedirection())
				.andExpect(model().attribute("status", "success"))
				.andExpect(model().attribute("application-no", "NAPIX/32145"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"));
	}

	// ----- test for 'startSigningProcess' method

	@Test
	final void testStartSigningProcess_ApplicationNoInRequestBody_IsNull() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "signingPerson", "Test user");

		mvc.perform(construct(request)).andExpect(status().is3xxRedirection())
				.andExpect(handler().methodName("startSigningProcess"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", containsStringIgnoringCase("Error count is 1")));
	}

	@Test
	final void testStartSigningProcess_ApplicationNoInRequestBody_IsBlank() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "applicationNo", "");
		ReflectionTestUtils.setField(request, "signingPerson", "Testing User");

		mvc.perform(construct(request)).andExpect(status().is3xxRedirection())
				.andExpect(handler().methodName("startSigningProcess"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", containsStringIgnoringCase("Error count is 2")));
	}

	@Test
	final void testStartSigningProcess_ApplicationNoInRequestBody_InvalidFormat() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "applicationNo", "NAPIX-789877");
		ReflectionTestUtils.setField(request, "signingPerson", "Test User");

		mvc.perform(construct(request)).andExpect(status().is3xxRedirection())
				.andExpect(handler().methodName("startSigningProcess"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", containsStringIgnoringCase("Error count is 1")));
	}

	@Test
	final void testStartSigningProcess_ApplicationNoInRequestBody_AllValid() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "applicationNo", "NAPIX/20001");
		ReflectionTestUtils.setField(request, "signingPerson", "Test User");

		MvcResult result = mvc.perform(construct(request)).andExpect(view().name("esign/redirecttogateway"))
				.andExpect(model().attribute("signedXml", "Valid Xml Request is made and signed"))
				.andExpect(model().attribute("nameOfUser", "Test User"))
				.andExpect(model().attribute("eSignGatewayUrl", eSignGatewayUrl)).andReturn();

		assertThat(Utils.returnBaseUrl(result.getRequest(), domainName, subDirectoryName)
				+ "/services/esign/process/gateway-response")
						.isEqualTo(result.getModelAndView().getModelMap().getAttribute("aspRedirectUrl"));
	}

	@Test
	final void testStartSigningProcess_SigningPersonInRequestBody_IsNull() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "applicationNo", "NAPIX/98745");

		mvc.perform(construct(request)).andExpect(status().is3xxRedirection())
				.andExpect(handler().methodName("startSigningProcess"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", containsStringIgnoringCase("Error count is 1")));
	}

	@Test
	final void testStartSigningProcess_SigningPersonInRequestBody_IsBlank() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "applicationNo", "NAPIX/98754");
		ReflectionTestUtils.setField(request, "signingPerson", "");

		mvc.perform(construct(request)).andExpect(status().is3xxRedirection())
				.andExpect(handler().methodName("startSigningProcess"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", containsStringIgnoringCase("Error count is 1")));
	}

	@Test
	final void testStartSigningProcess_SigningPersonInRequestBody_InvalidFormat() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "applicationNo", "NAPIX/78987");
		ReflectionTestUtils.setField(request, "signingPerson", "Test ser 213");

		mvc.perform(construct(request)).andExpect(status().is3xxRedirection())
				.andExpect(handler().methodName("startSigningProcess"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", containsStringIgnoringCase("Error count is 1")));
	}

	@Test
	final void testStartSigningProcess_ApplicationNoAndSigningPersonInRequestBody_IsInvalid() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "applicationNo", "NAPIX78987");
		ReflectionTestUtils.setField(request, "signingPerson", "Testing 12345");

		mvc.perform(construct(request)).andExpect(status().is3xxRedirection())
				.andExpect(handler().methodName("startSigningProcess"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", containsStringIgnoringCase("Error count is 2")));
	}

	@Test
	final void testStartSigningProcess_ApplicationNo_Invalid() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "applicationNo", "NAPIX/98745");
		ReflectionTestUtils.setField(request, "signingPerson", "User Test");

		mvc.perform(construct(request, new IllegalArgumentException("Invalid application reference number")))
				.andExpect(status().is3xxRedirection()).andExpect(handler().methodName("startSigningProcess"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed")).andExpect(model().attribute("error-message",
						containsString("Application number NAPIX/98745 is invalid!")));
	}

	@Test
	final void testStartSigningProcess_RuntimeExceptionOccured() throws Exception {
		WebController.ESignRequestBody request = new WebController.ESignRequestBody();
		ReflectionTestUtils.setField(request, "applicationNo", "NAPIX/98745");
		ReflectionTestUtils.setField(request, "signingPerson", "Using Test");

		mvc.perform(construct(request, new RuntimeException())).andExpect(status().is3xxRedirection())
				.andExpect(handler().methodName("startSigningProcess"))
				.andExpect(view().name("redirect:" + reactApplicationUrl + "/esign/process-response"))
				.andExpect(model().attribute("status", "failed"))
				.andExpect(model().attribute("error-message", containsString("Unknown Exception occured!")));
	}

	private MockHttpServletRequestBuilder construct(WebController.ESignRequestBody body)
			throws ParseException, UnsupportedEncodingException, IOException {
		return post("/services/esign/process/esign-form").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(EntityUtils.toString(new UrlEncodedFormEntity(
						Arrays.asList(new BasicNameValuePair("applicationNo", body.getApplicationNo()),
								new BasicNameValuePair("signingPerson", body.getSigningPerson())))))
				.with(proc -> {

					try {
						when(service.getSignedRequestInXml(body.getApplicationNo(), body.getSigningPerson(), proc))
								.thenReturn("Valid Xml Request is made and signed");
					} catch (IOException | DocumentException e) {
						e.printStackTrace();
					}

					return proc;
				});
	}

	private MockHttpServletRequestBuilder construct(WebController.ESignRequestBody body, Exception exception)
			throws ParseException, UnsupportedEncodingException, IOException {
		return post("/services/esign/process/esign-form").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.content(EntityUtils.toString(new UrlEncodedFormEntity(
						Arrays.asList(new BasicNameValuePair("applicationNo", body.getApplicationNo()),
								new BasicNameValuePair("signingPerson", body.getSigningPerson())))))
				.with(proc -> {

					try {
						when(service.getSignedRequestInXml(body.getApplicationNo(), body.getSigningPerson(), proc))
								.thenThrow(exception);
					} catch (IOException | DocumentException e) {
						e.printStackTrace();
					}

					return proc;
				});
	}

}
