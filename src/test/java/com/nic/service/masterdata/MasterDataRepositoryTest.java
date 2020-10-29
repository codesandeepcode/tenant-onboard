package com.nic.service.masterdata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import com.nic.service.DataJpaTestWrapper;

@Import(MasterDataRepository.class)
class MasterDataRepositoryTest extends DataJpaTestWrapper {

	@Autowired
	private MasterDataRepository repository;

	@Test
	void testFetchMasterStateList_NotNull() {
		assertNotNull(repository.fetchMasterStateList());
	}

	@Test
	void testFetchMasterStateList_HasExactSize() {
		assertThat(repository.fetchMasterStateList()).hasSize(35);
	}

	@Test
	void testFetchMasterStateList_FecthListOutputIsValid() {
		List<MasterStateModel> list = repository.fetchMasterStateList();
		MasterStateModel model = list.stream().filter(data -> data.getCode().equals("08")).findFirst().get();
		assertThat(model.getName()).isEqualTo("RAJASTHAN");

		MasterStateModel model2 = list.stream().filter(data -> data.getCode().equals("04")).findFirst().get();
		assertThat(model2.getName()).isEqualTo("CHANDIGARH");
	}

	@Test
	void testFetchMasterOfficeCategoryList_NotNull() {
		assertNotNull(repository.fetchMasterOfficeCategoryList());
	}

	@Test
	void testFetchMasterOfficeCategoryList_HasExactSize() {
		assertThat(repository.fetchMasterOfficeCategoryList()).hasSize(8);
	}

	@Test
	void testFetchMasterOfficeCategoryList_FetchListOutputIsValid() {
		List<MasterOfficeCategoryModel> list = repository.fetchMasterOfficeCategoryList();

		MasterOfficeCategoryModel model = list.stream().filter(data -> data.getId() == 5).findFirst().get();
		assertThat(model.getName()).isEqualTo("Public sector undertakings");
	}

	@Test
	void testFetchMasterDepartmentList_GivenCentralTypeAsInput_ExactSize() {
		assertThat(repository.fetchMasterDepartmentList("central").get()).hasSize(2);
	}

	@Test
	void testFetchMasterDepartmentList_GivenCentralTypeAsInput_Capitalized_ExactSize() {
		assertThat(repository.fetchMasterDepartmentList("CENTRAL").get()).hasSize(2);
	}

	@Test
	void testFetchMasterDepartmentList_GivenStateTypeInput_ExactSize() {
		assertThat(repository.fetchMasterDepartmentList("state").get()).hasSize(3);
	}

	@Test
	void testFetchMasterDepartmentListForStateCapital_ExactSize() {
		assertThat(repository.fetchMasterDepartmentList("STATE").get()).hasSize(3);
	}

	@Test
	void testFetchMasterDepartmentList_GivenWrongTypeValue() {
		assertThat(repository.fetchMasterDepartmentList("random")).isNotPresent();
	}

	@Test
	void testFetchMasterDepartmentList_GivenNullTypeValue() {
		assertThat(repository.fetchMasterDepartmentList(null)).isNotPresent();
	}

}
