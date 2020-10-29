package com.nic.service.onboarding.publisher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nic.service.esign.pdf.GovtApplicationModel;
import com.nic.service.esign.pdf.OthersApplicationModel;
import com.nic.service.utils.OrganisationType;
import com.nic.service.utils.RankTitle;

/**
 * This role of this class should only be managing the database, such as Create,
 * Read, Update, etc. No business logic should be present in this class.
 * 
 * @author Georgie
 * @since August 2020
 *
 */
@Repository
class PublisherRepository {

	private final DataMapper mapper;

	private final NamedParameterJdbcTemplate namedJdbcTemplate;

	@PersistenceContext
	private EntityManager em;

	public PublisherRepository(DataMapper mapper, DataSource dataSource) {
		this.mapper = mapper;
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private String generateApplicationReferenceNo() {
		return namedJdbcTemplate.getJdbcOperations().queryForObject("SELECT generate_reference_no();", String.class);
	}

	@Transactional
	public String save(NicApplicationFormModel formData) {
		PublisherApplicationEntity applicationEntity = mapper.map(generateApplicationReferenceNo(), formData);
		em.persist(applicationEntity);

		// Save NIC office details
		em.persist(mapper.map(applicationEntity, formData));

		// Save NIC employee details
		em.persist(mapper.map(applicationEntity, formData.getHog(), RankTitle.HEAD_OF_GROUP));
		em.persist(mapper.map(applicationEntity, formData.getHod(), RankTitle.HEAD_OF_DIVISION));
		em.persist(mapper.map(applicationEntity, formData.getTechAdmin1(), RankTitle.TECHNICAL_ADMIN));

		if (formData.getTechAdmin2() != null && StringUtils.isNotBlank(formData.getTechAdmin2().getCode())) {
			em.persist(mapper.map(applicationEntity, formData.getTechAdmin2(), RankTitle.TECHNICAL_ADMIN));
		}

		return applicationEntity.getApplicationReferenceNo();
	}

	@Transactional
	public String save(GovernmentApplicationFormModel formData) {
		PublisherApplicationEntity applicationEntity = mapper.map(generateApplicationReferenceNo(), formData);
		em.persist(applicationEntity);

		// Save Govt office details
		em.persist(mapper.map(applicationEntity, formData));

		// Save Govt personnel details
		List<GovtPersonnelEntity> personnelList = new ArrayList<>(4);
		personnelList.add(mapper.mapProjectHead(applicationEntity, formData));
		formData.getTechnicalHeadList()
				.forEach(head -> personnelList.add(mapper.mapTechnicalHeadGovt(applicationEntity, head)));

		personnelList.forEach(data -> em.persist(data));

		return applicationEntity.getApplicationReferenceNo();
	}

	@Transactional
	public String save(OthersApplicationFormModel formData) {
		PublisherApplicationEntity applicationEntity = mapper.map(generateApplicationReferenceNo(), formData);
		em.persist(applicationEntity);

		// Save Others office details
		em.persist(mapper.map(applicationEntity, formData));

		// Save Others personnel details
		List<OthersPersonnelEntity> personnelList = new ArrayList<>(4);
		personnelList.add(mapper.map(applicationEntity, formData.getProjectHead()));
		formData.getTechnicalHeadList()
				.forEach(head -> personnelList.add(mapper.mapTechnicalHeadOther(applicationEntity, head)));

		personnelList.forEach(em::persist);

		return applicationEntity.getApplicationReferenceNo();
	}

	@Transactional(readOnly = true)
	public boolean isDomainNameAlreadyRegistered(String domainName) {
		String query = "SELECT EXISTS(SELECT 1 FROM publisher_application_details WHERE domain_name=:domain_name);";
		Boolean result = namedJdbcTemplate.queryForObject(query, new MapSqlParameterSource("domain_name", domainName),
				Boolean.class);

		if (result == null)
			throw new RuntimeException("Result returned 'null' when testing for existance of '" + domainName + "'");

		return BooleanUtils.toBoolean(result);
	}

	@Transactional(readOnly = true)
	public Optional<String> getApplicationReferenceNumber(Integer applicationReferenceId) {
		PublisherApplicationEntity application = em.find(PublisherApplicationEntity.class, applicationReferenceId);
		return (application == null ? Optional.empty() : Optional.of(application.getApplicationReferenceNo()));
	}

	@Transactional(readOnly = true)
	public Optional<Integer> getApplicationReferenceId(String applicationReferenceNo) {
		final String query = "SELECT application_id FROM publisher_application_details WHERE application_reference_no=:application_no";
		SqlParameterSource param = new MapSqlParameterSource("application_no", applicationReferenceNo);

		return namedJdbcTemplate.query(query, param, new ResultSetExtractor<Optional<Integer>>() {

			@Override
			public Optional<Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next())
					return Optional.of(rs.getInt(1));

				return Optional.empty();
			}

		});
	}

	@Transactional(readOnly = true)
	public Optional<NicApplicationFormModel> fetchNicApplicationModel(Integer applicationReferenceId) {
		NicOfficeEntity office = em.find(NicOfficeEntity.class, applicationReferenceId);
		if (office == null)
			return Optional.empty();

		List<NicPersonnelEntity> list = em.createQuery(
				"SELECT a FROM NicPersonnelEntity a WHERE a.application.applicationId=:applicationId ORDER BY a.id",
				NicPersonnelEntity.class).setParameter("applicationId", applicationReferenceId).getResultList();

		NicPersonnelEntity hog = null, hod = null, techAdmin1 = null, techAdmin2 = null;
		for (NicPersonnelEntity data : list) {
			if (RankTitle.HEAD_OF_GROUP.equalTo(data.getTitleId())) {
				hog = data;
				continue;
			}

			if (RankTitle.HEAD_OF_DIVISION.equalTo(data.getTitleId())) {
				hod = data;
				continue;
			}
			if (RankTitle.TECHNICAL_ADMIN.equalTo(data.getTitleId()) && techAdmin1 == null) {
				techAdmin1 = data;
				continue;
			}

			if (RankTitle.TECHNICAL_ADMIN.equalTo(data.getTitleId()) && techAdmin2 == null) {
				techAdmin2 = data;
			}
		}

		PublisherApplicationEntity application = em.find(PublisherApplicationEntity.class, applicationReferenceId);

		return Optional.of(mapper.map(application, office, hog, hod, techAdmin1, techAdmin2));
	}

	@Transactional(readOnly = true)
	public Optional<GovtApplicationModel> fetchGovtApplicationModel(Integer applicationReferenceId) {
		GovtOfficeEntity office = em.find(GovtOfficeEntity.class, applicationReferenceId);
		if (office == null)
			return Optional.empty();

		List<GovtPersonnelEntity> list = em.createQuery(
				"SELECT a FROM GovtPersonnelEntity a WHERE a.application.applicationId=:applicationId ORDER BY a.id",
				GovtPersonnelEntity.class).setParameter("applicationId", applicationReferenceId).getResultList();

		GovtPersonnelEntity projectHead = list.stream()
				.filter(data -> RankTitle.PROJECT_HEAD.equalTo(data.getTitleId())).findFirst()
				.orElseThrow(() -> new RuntimeException("Project Head is not specified for government application"));

		List<GovtPersonnelEntity> technicalHeads = list.stream()
				.filter(data -> RankTitle.TECHNICAL_HEAD.equalTo(data.getTitleId())).collect(Collectors.toList());

		PublisherApplicationEntity application = em.find(PublisherApplicationEntity.class, applicationReferenceId);

		return Optional.of(mapper.map(application, office, projectHead, technicalHeads));
	}

	@Transactional(readOnly = true)
	public Optional<OthersApplicationModel> fetchOthersApplicationModel(Integer applicationReferenceId) {
		OthersOfficeEntity office = em.find(OthersOfficeEntity.class, applicationReferenceId);
		if (office == null)
			return Optional.empty();

		List<OthersPersonnelEntity> list = em.createQuery(
				"SELECT a FROM OthersPersonnelEntity a WHERE a.application.applicationId=:applicationId ORDER BY a.id",
				OthersPersonnelEntity.class).setParameter("applicationId", applicationReferenceId).getResultList();

		OthersPersonnelEntity projectHead = list.stream()
				.filter(data -> RankTitle.PROJECT_HEAD.equalTo(data.getTitleId())).findFirst()
				.orElseThrow(() -> new RuntimeException("Project Head is not specified for government application"));

		List<OthersPersonnelEntity> technicalHeads = list.stream()
				.filter(data -> RankTitle.TECHNICAL_HEAD.equalTo(data.getTitleId())).collect(Collectors.toList());

		PublisherApplicationEntity application = em.find(PublisherApplicationEntity.class, applicationReferenceId);

		return Optional.of(mapper.map(application, office, projectHead, technicalHeads));
	}

	@Transactional(readOnly = true)
	public Optional<LocalDateTime> getApplicationDateOfCreation(Integer applicationId) {
		PublisherApplicationEntity entity = em.find(PublisherApplicationEntity.class, applicationId);
		if (entity == null)
			return Optional.empty();

		return Optional.of(entity.getCreationDate());
	}

	@Transactional(readOnly = true)
	public boolean checkOrganisationTypeOfApplication(OrganisationType type, Integer applicationId) {
		switch (type) {
		case NIC:
			return doesApplicationExists(NicOfficeEntity.class, applicationId);
		case GOVT:
			return doesApplicationExists(GovtOfficeEntity.class, applicationId);
		case OTHERS:
			return doesApplicationExists(OthersOfficeEntity.class, applicationId);
		}

		throw new RuntimeException("Invalid type provided! Maybe you forgot to add missing case condition");
	}

	private <T> boolean doesApplicationExists(Class<T> entityClass, Integer applicationId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> root = cq.from(entityClass);

		cq.select(cb.count(root)).where(cb.equal(root.get("application"), applicationId));

		return em.createQuery(cq).getSingleResult() > 0;
	}

}
