package com.nic.service.onboarding.publisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nic.service.esign.pdf.GovtApplicationDataMother;
import com.nic.service.esign.pdf.GovtApplicationModel;
import com.nic.service.esign.pdf.OtherApplicationDataMother;
import com.nic.service.esign.pdf.OthersApplicationModel;
import com.nic.service.utils.OrganisationType;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

	private static Long timeLimit = 7L;

	static {
		timeLimit = RandomUtils.nextLong(1L, 30L);
	}

	@Mock
	private PublisherRepository repository;

	@Spy
	private Environment environment = new MockEnvironment().withProperty("publisher.application.max-time-limit",
			timeLimit.toString());

	@Spy
	private ObjectMapper mapper = new ObjectMapper();

	@InjectMocks
	private PublisherService service;

	@Test
	@Disabled
	void testSaveNicApplication() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testSaveGovernmentApplication() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testSaveOthersApplication() {
		fail("Not yet implemented");
	}

	@Test
	void testIsDomainNameRegisteredInDatabase_DomainNameIsRegistered() {
		when(repository.isDomainNameAlreadyRegistered("napix.nic.in")).thenReturn(true);
		assertThat(service.isDomainNameRegisteredInDatabase("napix.nic.in")).isTrue();
	}

	@Test
	void testIsDomainNameRegisteredInDatabase_DomainNameIsNotRegistered() {
		when(repository.isDomainNameAlreadyRegistered("napix.nic.in")).thenReturn(false);
		assertThat(service.isDomainNameRegisteredInDatabase("napix.nic.in")).isFalse();
	}

	@Test
	void testDoesApplicationExists_ApplicationNo_IsValid() {
		when(repository.getApplicationReferenceId("NAPIX/20212")).thenReturn(Optional.of(987));
		assertThat(service.doesApplicationExists("NAPIX/20212")).isTrue();
		verify(repository).getApplicationReferenceId("NAPIX/20212");
	}

	@Test
	void testDoesApplicationExists_ApplicationNo_IsNotValid() {
		when(repository.getApplicationReferenceId("NAPIX/20298")).thenReturn(Optional.empty());
		assertThat(service.doesApplicationExists("NAPIX/20298")).isFalse();
		verify(repository).getApplicationReferenceId("NAPIX/20298");
	}

	@Test
	void testGetApplicationReferenceNumber_ValidApplicationId() {
		when(repository.getApplicationReferenceNumber(987)).thenReturn(Optional.of("NAPIX/20154"));
		assertThat(service.getApplicationReferenceNumber(987)).isEqualTo("NAPIX/20154");
		verify(repository).getApplicationReferenceNumber(987);
	}

	@Test
	void testGetApplicationReferenceNumber_InvalidApplicationId() {
		when(repository.getApplicationReferenceNumber(878)).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> service.getApplicationReferenceNumber(878));
		verify(repository).getApplicationReferenceNumber(878);
	}

	@Test
	void testGetApplicationReferenceId_InvalidApplicationNo() {
		when(repository.getApplicationReferenceId("NAPIX/12545")).thenReturn(Optional.empty());
		assertThrows(IllegalArgumentException.class, () -> service.getApplicationReferenceId("NAPIX/12545"));
		verify(repository).getApplicationReferenceId("NAPIX/12545");
	}

	@Test
	void testGetApplicationReferenceId_ValidApplicationNo() {
		when(repository.getApplicationReferenceId("NAPIX/98745")).thenReturn(Optional.of(6544));
		assertThat(service.getApplicationReferenceId("NAPIX/98745")).isEqualTo(6544);
		verify(repository).getApplicationReferenceId("NAPIX/98745");
	}

	@Test
	void testFetchApplicationModelForNic_ValidApplicationNo() {
		NicApplicationFormModel expected = NicApplicationFormModelObjectMother.defaultApplication().build();
		when(repository.fetchNicApplicationModel(98745)).thenReturn(Optional.of(expected));
		when(repository.getApplicationReferenceId("NAPIX/987654")).thenReturn(Optional.of(98745));

		NicApplicationFormModel actual = service.fetchApplicationModelForNic("NAPIX/987654");
		assertThat(actual.getDomain()).isEqualTo(expected.getDomain());
		assertThat(actual.getHod().getCode()).isEqualTo(actual.getHod().getCode());

		verify(repository).fetchNicApplicationModel(98745);
		verify(repository).getApplicationReferenceId("NAPIX/987654");
	}

	@Test
	void testFetchApplicationModelForNic_InvalidApplicationNo() {
		when(repository.getApplicationReferenceId("NAPIX/987654")).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> service.fetchApplicationModelForNic("NAPIX/987654"));
		verify(repository).getApplicationReferenceId("NAPIX/987654");
		verify(repository, times(0)).fetchNicApplicationModel(ArgumentMatchers.anyInt());
	}

	@Test
	void testFetchApplicationModelForGovt_ValidApplicationNo() {
		GovtApplicationModel expected = GovtApplicationDataMother.aDefaultApplication().build();
		when(repository.fetchGovtApplicationModel(456)).thenReturn(Optional.of(expected));
		when(repository.getApplicationReferenceId("NAPIX/321654")).thenReturn(Optional.of(456));

		GovtApplicationModel actual = service.fetchApplicationModelForGovt("NAPIX/321654");
		assertThat(actual.getDomainName()).isEqualTo(expected.getDomainName());
		assertThat(actual.getProjectHead().getCode()).isEqualTo(actual.getProjectHead().getCode());
		assertThat(actual.getTechnicalHeadList().get(0).getEmailId())
				.isEqualTo(actual.getTechnicalHeadList().get(0).getEmailId());

		verify(repository).fetchGovtApplicationModel(456);
		verify(repository).getApplicationReferenceId("NAPIX/321654");
	}

	@Test
	void testFetchApplicationModelForGovt_InvalidApplicationNo() {
		when(repository.getApplicationReferenceId("NAPIX/200012")).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> service.fetchApplicationModelForGovt("NAPIX/200012"));
		verify(repository).getApplicationReferenceId("NAPIX/200012");
		verify(repository, times(0)).fetchGovtApplicationModel(ArgumentMatchers.anyInt());
	}

	@Test
	void testFetchApplicationModelForOthers_ValidApplicationNo() {
		OthersApplicationModel expected = OtherApplicationDataMother.aDefaultApplication().build();
		when(repository.fetchOthersApplicationModel(123)).thenReturn(Optional.of(expected));
		when(repository.getApplicationReferenceId("NAPIX/789878")).thenReturn(Optional.of(123));

		OthersApplicationModel actual = service.fetchApplicationModelForOthers("NAPIX/789878");
		assertThat(actual.getDomainName()).isEqualTo(expected.getDomainName());
		assertThat(actual.getProjectHead().getCode()).isEqualTo(actual.getProjectHead().getCode());
		assertThat(actual.getTechnicalHead().get(0).getEmailId())
				.isEqualTo(actual.getTechnicalHead().get(0).getEmailId());

		verify(repository).fetchOthersApplicationModel(123);
		verify(repository).getApplicationReferenceId("NAPIX/789878");
	}

	@Test
	void testFetchApplicationModelForOthers_InvalidApplicationNo() {
		when(repository.getApplicationReferenceId("NAPIX/200012")).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> service.fetchApplicationModelForOthers("NAPIX/200012"));
		verify(repository).getApplicationReferenceId("NAPIX/200012");
		verify(repository, times(0)).fetchOthersApplicationModel(ArgumentMatchers.anyInt());
	}

	@Test
	final void testHasApplicationExceedTimeLimit_NullApplicationNoProvided() {
		when(repository.getApplicationReferenceId(null)).thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> service.hasApplicationExceedTimeLimit(null));
		assertThat(ex.getMessage()).contains("Invalid application reference number provided");

		verify(repository, times(0)).getApplicationDateOfCreation(ArgumentMatchers.anyInt());
	}

	@Test
	final void testHasApplicationExceedTimeLimit_InvalidApplicationNoProvided() {
		when(repository.getApplicationReferenceId("NAPIX/12345")).thenReturn(Optional.empty());

		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> service.hasApplicationExceedTimeLimit("NAPIX/12345"));
		assertThat(ex.getMessage()).contains("Invalid application reference number provided - NAPIX/12345");

		verify(repository, times(0)).getApplicationDateOfCreation(ArgumentMatchers.anyInt());
	}

	@Test
	final void testHasApplicationExceedTimeLimit_NoCreationDateProvided() {
		when(repository.getApplicationReferenceId("NAPIX/3216")).thenReturn(Optional.of(9871));
		when(repository.getApplicationDateOfCreation(9871)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> service.hasApplicationExceedTimeLimit("NAPIX/3216"));
	}

	@Test
	final void testHasApplicationExceedTimeLimit_ValidApplicationNo_BeforeDueDate() {
		when(repository.getApplicationReferenceId("NAPIX/321654")).thenReturn(Optional.of(987));
		when(repository.getApplicationDateOfCreation(987))
				.thenReturn(Optional.of(LocalDateTime.now().minusDays(timeLimit - 5L)));

		assertFalse(service.hasApplicationExceedTimeLimit("NAPIX/321654"));
	}

	@Test
	final void testHasApplicationExceedTimeLimit_ValidApplicationNo_JustInDueDate() {
		when(repository.getApplicationReferenceId("NAPIX/98745")).thenReturn(Optional.of(4511));
		when(repository.getApplicationDateOfCreation(4511))
				.thenReturn(Optional.of(LocalDateTime.now().minusDays(timeLimit)));

		assertFalse(service.hasApplicationExceedTimeLimit("NAPIX/98745"));
	}

	@Test
	final void testHasApplicationExceedTimeLimit_ValidApplicationNo_BeyondDueDate() {
		when(repository.getApplicationReferenceId("NAPIX/98745")).thenReturn(Optional.of(4511));
		when(repository.getApplicationDateOfCreation(4511))
				.thenReturn(Optional.of(LocalDateTime.now().minusDays(timeLimit + 1L)));

		assertTrue(service.hasApplicationExceedTimeLimit("NAPIX/98745"));
	}

	@Test
	void testDetermineOrganisationType_InvalidApplicationNo() {
		when(repository.getApplicationReferenceId("NAPIX/200012")).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> service.determineOrganisationType("NAPIX/200012"));
		verify(repository, times(0)).checkOrganisationTypeOfApplication(ArgumentMatchers.any(),
				ArgumentMatchers.anyInt());
	}

	@Test
	void testDetermineOrganisationType_ValidApplicationNo_NicType() {
		when(repository.getApplicationReferenceId("NAPIX/200012")).thenReturn(Optional.of(32145));
		when(repository.checkOrganisationTypeOfApplication(ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
				.thenReturn(false);
		when(repository.checkOrganisationTypeOfApplication(OrganisationType.NIC, 32145)).thenReturn(true);

		assertThat(service.determineOrganisationType("NAPIX/200012").name()).isEqualTo("NIC");
	}

	@Test
	void testDetermineOrganisationType_ValidApplicationNo_GovtType() {
		when(repository.getApplicationReferenceId("NAPIX/200878")).thenReturn(Optional.of(345));
		when(repository.checkOrganisationTypeOfApplication(ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
				.thenReturn(false);
		when(repository.checkOrganisationTypeOfApplication(OrganisationType.GOVT, 345)).thenReturn(true);

		assertThat(service.determineOrganisationType("NAPIX/200878").name()).isEqualTo("GOVT");
	}

	@Test
	void testDetermineOrganisationType_ValidApplicationNo_OthersType() {
		when(repository.getApplicationReferenceId("NAPIX/2000144")).thenReturn(Optional.of(3214));
		when(repository.checkOrganisationTypeOfApplication(ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
				.thenReturn(false);
		when(repository.checkOrganisationTypeOfApplication(OrganisationType.OTHERS, 3214)).thenReturn(true);

		assertThat(service.determineOrganisationType("NAPIX/2000144").name()).isEqualTo("OTHERS");
	}

	@Test
	void testDetermineOrganisationType_ValidApplicationNo_NoTypeMatched() {
		when(repository.getApplicationReferenceId("NAPIX/207888")).thenReturn(Optional.of(4577));
		when(repository.checkOrganisationTypeOfApplication(ArgumentMatchers.any(), ArgumentMatchers.anyInt()))
				.thenReturn(false);

		assertThrows(RuntimeException.class, () -> service.determineOrganisationType("NAPIX/207888"));
	}

}
