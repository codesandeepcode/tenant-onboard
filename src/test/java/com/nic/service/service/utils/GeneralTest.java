package com.nic.service.service.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.nic.service.utils.GovernmentType;
import com.nic.service.utils.Utils;

class GeneralTest {

	@Test
	final void testGivernmentTypeById() {
		assertTrue(GovernmentType.getById((short) 1) == GovernmentType.CENTRAL);
	}

	@Test
	final void testGivernmentTypeByWrongId() {
		String message = assertThrows(IllegalArgumentException.class, () -> GovernmentType.getById((short) 4))
				.getMessage();
		assertThat(message).isEqualTo("Invalid id provided - 4");
	}

	@Test
	final void testReturnBaseUrl_NoDomainNameAndSubdirectory() throws UnknownHostException {
		MockHttpServletRequest req = new MockHttpServletRequest();

		assertThat(Utils.returnBaseUrl(req, "", "")).isEqualTo("http://" + InetAddress.getLocalHost().getHostAddress());
		assertThat(Utils.returnBaseUrl(req, null, null))
				.isEqualTo("http://" + InetAddress.getLocalHost().getHostAddress());
	}

	@Test
	final void testReturnBaseUrl_NoDomainNameAndSubdirectoryWithPort() throws UnknownHostException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setServerPort(8000);

		assertThat(Utils.returnBaseUrl(req, "", null))
				.isEqualTo("http://" + InetAddress.getLocalHost().getHostAddress() + ":8000");
	}

	@Test
	final void testReturnBaseUrl_GivenDomainNameAndNoSubdirectory() {
		MockHttpServletRequest req = new MockHttpServletRequest();

		assertThat(Utils.returnBaseUrl(req, "napix.gov.in", null)).isEqualTo("http://napix.gov.in");
		assertThat(Utils.returnBaseUrl(req, "napix.gov.in", "")).isEqualTo("http://napix.gov.in");
		assertThat(Utils.returnBaseUrl(req, "localhost", "")).isEqualTo("http://localhost");
	}

	@Test
	final void testReturnBaseUrl_GivenDomainNameAndNoSubdirectoryWithPort() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setServerPort(8080);

		assertThat(Utils.returnBaseUrl(req, "napix.gov.in", null)).isEqualTo("http://napix.gov.in:8080");
		assertThat(Utils.returnBaseUrl(req, "napix.gov.in", "")).isEqualTo("http://napix.gov.in:8080");
		assertThat(Utils.returnBaseUrl(req, "localhost", null)).isEqualTo("http://localhost:8080");
	}

	@Test
	final void testReturnBaseUrl_GivenDomainNameAndSubdirectory() {
		MockHttpServletRequest req = new MockHttpServletRequest();

		assertThat(Utils.returnBaseUrl(req, "localhost", "onboard")).isEqualTo("http://localhost/onboard");
		assertThat(Utils.returnBaseUrl(req, "napix.gov.in", "onboard")).isEqualTo("http://napix.gov.in/onboard");
	}

	@Test
	final void testReturnBaseUrl_GivenDomainNameAndSubdirectoryWithPort() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setServerPort(3000);

		assertThat(Utils.returnBaseUrl(req, "localhost", "onboard")).isEqualTo("http://localhost:3000/onboard");
		assertThat(Utils.returnBaseUrl(req, "napix.gov.in", "onboard")).isEqualTo("http://napix.gov.in:3000/onboard");
	}

	@Test
	final void testReturnBaseUrl_NoDomainNameAndValidSubdirectory() throws UnknownHostException {
		MockHttpServletRequest req = new MockHttpServletRequest();

		assertThat(Utils.returnBaseUrl(req, null, "testboard"))
				.isEqualTo("http://" + InetAddress.getLocalHost().getHostAddress() + "/testboard");
		assertThat(Utils.returnBaseUrl(req, "", "testboard"))
				.isEqualTo("http://" + InetAddress.getLocalHost().getHostAddress() + "/testboard");
	}

	@Test
	final void testReturnBaseUrl_NoDomainNameAndValidSubdirectoryWithPort() throws UnknownHostException {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setServerPort(5050);

		assertThat(Utils.returnBaseUrl(req, null, "testboard"))
				.isEqualTo("http://" + InetAddress.getLocalHost().getHostAddress() + ":5050/testboard");
		assertThat(Utils.returnBaseUrl(req, "", "testboard"))
				.isEqualTo("http://" + InetAddress.getLocalHost().getHostAddress() + ":5050/testboard");
	}
}
