package com.nic.service.masterdata;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@Disabled
@DataJpaTest
@Import(MasterDataRepository.class)
class MasterDataRepository_EmptyDbTest {

	@Autowired
	private MasterDataRepository repository;

	@Autowired
	private TestEntityManager em;

	@Test
	void testFetchMasterStateList_DbIsEmpty() {
		EntityManager m = em.getEntityManager();

		CriteriaBuilder cb = m.getCriteriaBuilder();
		CriteriaQuery<MasterStateEntity> cq = cb.createQuery(MasterStateEntity.class);

		assertThat(m.createQuery(cq.select(cq.from(MasterStateEntity.class))).getResultList()).isEmpty();
	}

	@Test
	void testFetchMasterStateList_WhenDbIsEmptyThenMethodReturnEmptyList() {
		assertThat(repository.fetchMasterStateList()).isNotNull();
		assertThat(repository.fetchMasterStateList()).isEmpty();
	}

	@Test
	void testFetchMasterOfficeCategoryList_WhenDbIsEmptyThenMethodReturnEmptyList() {
		assertThat(repository.fetchMasterOfficeCategoryList()).isNotNull();
		assertThat(repository.fetchMasterOfficeCategoryList()).isEmpty();
	}

	@Test
	void testFetchMasterDepartmentList_WhenDbIsEmptyThenMethodReturnEmptyList() {
		assertThat(repository.fetchMasterDepartmentList("central")).isNotNull();
		assertThat(repository.fetchMasterDepartmentList("central").get()).isEmpty();
	}

}
