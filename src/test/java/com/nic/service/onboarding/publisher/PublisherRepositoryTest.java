package com.nic.service.onboarding.publisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nic.service.DataJpaTestWrapper;
import com.nic.service.esign.pdf.GovtApplicationModel;
import com.nic.service.esign.pdf.OthersApplicationModel;
import com.nic.service.masterdata.MasterDepartmentEntity;
import com.nic.service.masterdata.MasterStateEntity;
import com.nic.service.utils.OrganisationType;

/**
 * @author Georgie
 *
 */
@Import({ PublisherRepository.class, OnBoardingDataMapperImpl.class })
class PublisherRepositoryTest extends DataJpaTestWrapper {

	@Autowired
	private PublisherRepository repository;

	@Autowired
	private TestEntityManager em;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private boolean match(String applicationNo) {
		return Pattern.compile("^NAPIX\\/[\\d]{5}$").matcher(applicationNo).matches();
	}

	private Integer getAppId(String appNo) {
		CriteriaBuilder cb = em.getEntityManager().getCriteriaBuilder();
		CriteriaQuery<PublisherApplicationEntity> cq = cb.createQuery(PublisherApplicationEntity.class);
		Root<PublisherApplicationEntity> root = cq.from(PublisherApplicationEntity.class);
		cq.select(root).where(cb.equal(root.get("applicationReferenceNo"), appNo));

		List<PublisherApplicationEntity> result = em.getEntityManager().createQuery(cq).getResultList();
		assertThat(result).hasSize(1);
		return result.get(0).getApplicationId();
	}

	@RepeatedTest(10)
	final void testSave_NicApplicationFormModel_FullDetails() {
		NicApplicationFormModel form = NicApplicationFormModelObjectMother.defaultApplication().build();
		String appNo = repository.save(form);
		Integer appId = getAppId(appNo);

		assertTrue(match(appNo));
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publisher_application_details",
				"application_id='" + appId + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publisher_application_details",
				"domain_name='" + form.getDomain() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "nic_office_details",
				"application_id='" + appId + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "nic_office_details",
				"group_name='" + form.getGroup() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "nic_personnel_details",
				"application_id='" + appId + "'")).isEqualTo(4);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "nic_personnel_details",
				"address='" + form.getTechAdmin1().getAddress() + "'")).isEqualTo(1);
	}

	@RepeatedTest(10)
	final void testSave_NicApplicationFormModel_MinimumDetails() {
		NicApplicationFormModel form = NicApplicationFormModelObjectMother.defaultApplication().withTechAdmin2(null)
				.build();
		String appNo = repository.save(form);
		Integer appId = getAppId(appNo);

		assertTrue(match(appNo));
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publisher_application_details",
				"application_id='" + appId + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publisher_application_details",
				"domain_name='" + form.getDomain() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "nic_office_details",
				"application_id='" + appId + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "nic_office_details",
				"group_name='" + form.getGroup() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "nic_personnel_details",
				"application_id='" + appId + "'")).isEqualTo(3);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "nic_personnel_details",
				"address='" + form.getTechAdmin1().getAddress() + "'")).isEqualTo(1);
	}

	@RepeatedTest(10)
	final void testSave_GovernmentApplicationFormModel_FullDetails() {
		GovernmentApplicationFormModel form = GovernmentApplicationFormModelObjectMother.defaultApplication(true)
				.build();
		String appNo = repository.save(form);
		Integer appId = getAppId(appNo);

		assertTrue(match(appNo));
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publisher_application_details",
				"domain_name='" + form.getDomainName() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "govt_office_details",
				"application_id='" + appId + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "govt_office_details",
				"application_id='" + appId + "' and office_name='" + form.getOffice().getName() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "govt_personnel_details",
				"application_id='" + appId + "'")).isEqualTo(4);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "govt_personnel_details", "application_id='"
				+ appId + "' and employee_name='" + form.getTechnicalHeadList().get(0).getName() + "'")).isEqualTo(1);
	}

	@RepeatedTest(10)
	final void testSave_GovernmentApplicationFormModel_MinimumDetails() {
		GovernmentApplicationFormModel form = GovernmentApplicationFormModelObjectMother.defaultApplication(false)
				.build();
		String appNo = repository.save(form);
		Integer appId = getAppId(appNo);

		assertTrue(match(appNo));
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publisher_application_details",
				"project_name='" + form.getProjectName() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "govt_office_details",
				"application_id='" + appId + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "govt_office_details",
				"application_id='" + appId + "' and office_address='" + form.getOffice().getAddress() + "'"))
						.isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "govt_personnel_details",
				"application_id='" + appId + "'")).isEqualTo(form.getTechnicalHeadList().size() + 1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "govt_personnel_details", "application_id='"
				+ appId + "' and email_id='" + form.getTechnicalHeadList().get(0).getEmailId() + "'")).isEqualTo(1);
	}

	@RepeatedTest(10)
	final void testSave_OthersApplicationFormModel_FullDetails() throws JsonProcessingException {
		OthersApplicationFormModel form = OthersApplicationFormModelObjectMother.defaultApplication(true).build();
		String appNo = repository.save(form);
		Integer appId = getAppId(appNo);

		assertTrue(match(appNo));
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publisher_application_details",
				"domain_name='" + form.getDomainName() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "others_office_details",
				"application_id='" + appId + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "others_office_details",
				"application_id='" + appId + "' and company_name='" + form.getOffice().getName() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "others_personnel_details",
				"application_id='" + appId + "'")).isEqualTo(4);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "others_personnel_details", "application_id='"
				+ appId + "' and mobile_number='" + form.getTechnicalHeadList().get(0).getMobileNo() + "'"))
						.isEqualTo(1);
	}

	@RepeatedTest(10)
	final void testSave_OthersApplicationFormModel_MinimumDetails() throws JsonProcessingException {
		OthersApplicationFormModel form = OthersApplicationFormModelObjectMother.defaultApplication(false).build();
		String appNo = repository.save(form);
		Integer appId = getAppId(appNo);

		assertTrue(match(appNo));
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "publisher_application_details",
				"domain_name='" + form.getDomainName() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "others_office_details",
				"application_id='" + appId + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "others_office_details",
				"application_id='" + appId + "' and state_id='" + form.getOffice().getState() + "'")).isEqualTo(1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "others_personnel_details",
				"application_id='" + appId + "'")).isEqualTo(form.getTechnicalHeadList().size() + 1);
		assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "others_personnel_details", "application_id='"
				+ appId + "' and employee_name='" + form.getTechnicalHeadList().get(0).getName() + "'")).isEqualTo(1);
	}

	@Test
	final void testIsDomainNameAlreadyRegistered_DomainNameExists() {
		assertTrue(repository.isDomainNameAlreadyRegistered("sqggroup.nic.in"));
	}

	@Test
	final void testIsDomainNameAlreadyRegistered_DomainNameDoesNotExists() {
		assertFalse(repository.isDomainNameAlreadyRegistered("random.nic.in"));
	}

	@Test
	final void testIsDomainNameAlreadyRegistered_MaliciousDomainNameProvided() {
		assertThat(repository.isDomainNameAlreadyRegistered("unknown' OR '1'='1")).isFalse();
	}

	@Test
	final void testGetApplicationReferenceNumber_ValidApplicationId() {
		assertThat(repository.getApplicationReferenceNumber(142)).get().isEqualTo("NAPIX/20104");
	}

	@Test
	final void testGetApplicationReferenceNumber_InvalidApplicationId() {
		assertThat(repository.getApplicationReferenceNumber(54)).isEmpty();
	}

	@Test
	final void testGetApplicationReferenceNumber_ApplicationIdIsNull() {
		assertThrows(IllegalArgumentException.class, () -> repository.getApplicationReferenceNumber(null));
	}

	@Test
	final void testGetApplicationReferenceId_ValidApplicationRefNo() {
		assertThat(repository.getApplicationReferenceId("NAPIX/20105")).get().isEqualTo(143);
	}

	@Test
	final void testGetApplicationReferenceId_InvalidApplicationRefNo() {
		assertThat(repository.getApplicationReferenceId("NAPIX/20045")).isEmpty();
	}

	@Test
	final void testGetApplicationReferenceId_ApplicationRefNoIsNull() {
		assertThat(repository.getApplicationReferenceId(null)).isEmpty();
	}

	@Test
	final void testGetApplicationReferenceId_MaliciousApplicationRefNoIsProvided() {
		assertThat(repository.getApplicationReferenceId("NAPIX/20015' OR '1'='1'--")).isEmpty();
	}

	@Test
	final void testfetchNicApplicationModel_ValidApplicationReferenceIdProvided() {
		Optional<NicApplicationFormModel> model = repository.fetchNicApplicationModel(141);

		assertThat(model).isNotEmpty();
		assertThat(model.get().getDomain()).isEqualTo("sqggroup.nic.in");
		assertThat(model.get().getTechAdmin1().getCode()).isEqualTo("5516");
		assertThat(model.get().getHog().getEmailId()).isEqualTo("pawan.joshi@nic.in");
		assertThat(model.get().getHod().getMobileNo()).isEqualTo("9810119461");
	}

	@Test
	final void testfetchNicApplicationModel_ValidApplicationReferenceIdProvided_NotNicType() {
		assertThat(repository.fetchNicApplicationModel(46)).isEmpty();
	}

	@Test
	final void testfetchNicApplicationModel_InvalidApplicationReferenceIdProvided() {
		assertThat(repository.fetchNicApplicationModel(184)).isEmpty();
	}

	@Test
	final void testfetchNicApplicationModel_ApplicationReferenceIdProvidedIsNull() {
		assertThrows(IllegalArgumentException.class, () -> repository.fetchNicApplicationModel(null));
	}

	@Test
	final void testFetchGovtApplicationModel_ApplicationReferenceIdProvidedIsNull() {
		assertThrows(IllegalArgumentException.class, () -> repository.fetchGovtApplicationModel(null));
	}

	@Test
	final void testFetchGovtApplicationModel_ApplicationReferenceIdProvidedIsValid() {
		Optional<GovtApplicationModel> model = repository.fetchGovtApplicationModel(144);

		assertThat(model).isNotEmpty();
		assertThat(model.get().getGovernmentType().getName()).isEqualTo("Central");
		assertThat(model.get().getDomainName()).isEqualTo("save-water.gov.in");
		assertThat(model.get().getProjectHead().getEmailId()).isEqualTo("vikassharma@gov.in");
		assertThat(model.get().getTechnicalHeadList().get(0).getCode()).isEqualTo("TV-123-11");
		assertThat(model.get().getOffice().getDepartment())
				.isEqualTo(em.find(MasterDepartmentEntity.class, 1).getName());
	}

	@Test
	final void testFetchGovtApplicationModel_ApplicationReferenceIdProvidedIsValid_NotGovtType() {
		assertThat(repository.fetchGovtApplicationModel(41)).isEmpty();
	}

	@Test
	final void testFetchGovtApplicationModel_ApplicationReferenceIdProvidedIsNotValid() {
		assertThat(repository.fetchGovtApplicationModel(124)).isEmpty();
	}

	@Test
	final void testFetchOthersApplicationModel_ApplicationReferenceIdProvidedIsNotValid() {
		assertThat(repository.fetchOthersApplicationModel(165)).isEmpty();
	}

	@Test
	final void testFetchOthersApplicationModel_ApplicationReferenceIdProvidedIsValid() {
		Optional<OthersApplicationModel> model = repository.fetchOthersApplicationModel(146);

		assertThat(model).isNotEmpty();
		assertThat(model.get().getOffice().getName()).isEqualTo("Free API Company");
		assertThat(model.get().getProjectHead().getEmailId()).isEqualTo("sign@google.com");
		assertThat(model.get().getTechnicalHead().get(0).getCode()).isEqualTo("YU-76-TYT");
		assertThat(model.get().getTechnicalHead().get(0).getMobileNo()).isEqualTo("9875454545");
		assertThat(model.get().getProjectHead().getName()).isEqualTo("Amrinder Singh");

		assertThat(model.get().getOffice().getState())
				.isEqualTo(em.find(MasterStateEntity.class, "17").getStateNameEn());
	}

	@Test
	final void testFetchOthersApplicationModel_ApplicationReferenceIdProvidedIsValid_NotOthersType() {
		assertThat(repository.fetchOthersApplicationModel(41)).isEmpty();
	}

	@Test
	final void testFetchOthersApplicationModel_ApplicationReferenceIdProvidedIsNull() {
		assertThrows(IllegalArgumentException.class, () -> repository.fetchOthersApplicationModel(null));
	}

	@Test
	final void testGetApplicationDateOfCreation_ApplicationReferenceIdProvidedIsNull() {
		assertThrows(IllegalArgumentException.class, () -> repository.getApplicationDateOfCreation(null));
	}

	@Test
	final void testGetApplicationDateOfCreation_ApplicationReferenceIdProvidedIsValid() {
		assertThat(repository.getApplicationDateOfCreation(142)).get()
				.isEqualTo(LocalDateTime.parse("2020-09-29T15:41:14.235753"));
	}

	@Test
	final void testGetApplicationDateOfCreation_ApplicationReferenceIdProvidedIsNotValid() {
		assertThat(repository.getApplicationDateOfCreation(154)).isEmpty();
	}

	@Test
	final void testCheckOrganisationTypeOfApplication_OrganisationTypeIsNic_ValidApplicationId() {
		assertThat(repository.checkOrganisationTypeOfApplication(OrganisationType.NIC, 143)).isTrue();
	}

	@Test
	final void testCheckOrganisationTypeOfApplication_OrganisationTypeIsNic_InvalidApplicationId() {
		assertThat(repository.checkOrganisationTypeOfApplication(OrganisationType.NIC, 987)).isFalse();
	}

	@Test
	final void testCheckOrganisationTypeOfApplication_OrganisationTypeIsGovt_ValidApplicationId() {
		assertThat(repository.checkOrganisationTypeOfApplication(OrganisationType.GOVT, 145)).isTrue();
	}

	@Test
	final void testCheckOrganisationTypeOfApplication_OrganisationTypeIsGovt_InvalidApplicationId() {
		assertThat(repository.checkOrganisationTypeOfApplication(OrganisationType.GOVT, 9878)).isFalse();
	}

	@Test
	final void testCheckOrganisationTypeOfApplication_OrganisationTypeIsOthers_ValidApplicationId() {
		assertThat(repository.checkOrganisationTypeOfApplication(OrganisationType.OTHERS, 146)).isTrue();
	}

	@Test
	final void testCheckOrganisationTypeOfApplication_OrganisationTypeIsOthers_InvalidApplicationId() {
		assertThat(repository.checkOrganisationTypeOfApplication(OrganisationType.OTHERS, 9874)).isFalse();
	}

	@Test
	final void testCheckOrganisationTypeOfApplication_OrganisationTypeIsNull_ExceptionThrown() {
		assertThrows(RuntimeException.class, () -> repository.checkOrganisationTypeOfApplication(null, 88));
	}

	/*
	 * 
	 * @Test final void
	 * testHasApplicationExceedTimeLimit_NullApplicationNoProvided() {
	 * IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()
	 * -> repository.hasApplicationExceedTimeLimit(null));
	 * assertThat(ex.getMessage()).contains("Application No is null or blank."); }
	 * 
	 * @Test final void
	 * testHasApplicationExceedTimeLimit_InvalidApplicationNoProvided() {
	 * IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()
	 * -> repository.hasApplicationExceedTimeLimit("NAPIX/12345"));
	 * assertThat(ex.getMessage()).contains("Invalid application no provided"); }
	 * 
	 * @Test final void
	 * testHasApplicationExceedTimeLimit_ValidApplicationNo_BeforeDueDate() {
	 * PublisherApplicationEntity application =
	 * PublisherApplicationEntityObjectMother.defaultApplication()
	 * .withCreationDate(LocalDateTime.now().minusDays(3L)).build();
	 * em.persist(application);
	 * 
	 * assertNotNull(application.getApplicationId());
	 * assertFalse(repository.hasApplicationExceedTimeLimit(application.
	 * getApplicationReferenceNo())); }
	 * 
	 * @Test final void
	 * testHasApplicationExceedTimeLimit_ValidApplicationNo_JustInDueDate() {
	 * PublisherApplicationEntity application =
	 * PublisherApplicationEntityObjectMother.defaultApplication()
	 * .withCreationDate(LocalDateTime.now().minusDays(7L)).build();
	 * em.persist(application);
	 * 
	 * assertNotNull(application.getApplicationId());
	 * assertFalse(repository.hasApplicationExceedTimeLimit(application.
	 * getApplicationReferenceNo())); }
	 * 
	 * @Test final void
	 * testHasApplicationExceedTimeLimit_ValidApplicationNo_BeyondDueDate() {
	 * PublisherApplicationEntity application =
	 * PublisherApplicationEntityObjectMother.defaultApplication()
	 * .withCreationDate(LocalDateTime.now().minusDays(8L)).build();
	 * em.persist(application);
	 * 
	 * assertTrue(repository.hasApplicationExceedTimeLimit(application.
	 * getApplicationReferenceNo())); }
	 * 
	 * 
	 */
}
