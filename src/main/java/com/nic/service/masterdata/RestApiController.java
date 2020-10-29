package com.nic.service.masterdata;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Validated
@RestController("masterRestApiController")
@RequestMapping("/api/")
public class RestApiController {

	private final MasterDataRepository repository;

	RestApiController(MasterDataRepository repository) {
		this.repository = repository;
	}

	@GetMapping("master/states")
	public List<MasterStateModel> getMasterStateList() {
		return repository.fetchMasterStateList();
	}

	@GetMapping("master/states/{stateId}")
	public MasterStateModel getMasterStateWithGivenId(
			@PathVariable @Pattern(regexp = "^[0-9]{2}$", message = "Illegal data provided!") String stateId) {
		return repository.fetchMasterStateList().stream().filter(data -> StringUtils.equals(data.getCode(), stateId))
				.findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Cannot find master State for given id - " + stateId));
	}

	@GetMapping("master/officecategories")
	public List<MasterOfficeCategoryModel> getMasterOfficeCategoryList() {
		return repository.fetchMasterOfficeCategoryList();
	}

	@GetMapping("master/officecategories/{officeId}")
	public MasterOfficeCategoryModel getMasterOfficeCategoryWithGivenId(
			@PathVariable @Pattern(regexp = "^[\\d]+$", message = "Illegal data provided!") String officeId) {
		Integer id = Integer.parseInt(officeId);
		return repository.fetchMasterOfficeCategoryList().stream().filter(data -> data.getId() == id).findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Cannot find master Office category for given id -" + officeId));
	}

	@GetMapping("master/departments/{govtType}")
	public List<MasterDepartmentModel> getMasterDepartmentList(
			@PathVariable @Pattern(regexp = "^(central|state)$", message = "Only 'central' or 'state' value are allowed!") String govtType) {
		return repository.fetchMasterDepartmentList(govtType)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
	}

	@GetMapping("master/departments/{governmentType}/{departmentId}")
	public MasterDepartmentModel getMasterDepartmentWithGivenId(
			@PathVariable @Pattern(regexp = "^(central|state)$", message = "Only 'central' or 'state' value are allowed!") String governmentType,
			@PathVariable @Pattern(regexp = "^[\\d]+$", message = "Illegal data provided! Should be integer only!") String departmentId) {
		Integer id = Integer.parseInt(departmentId);
		return repository.fetchMasterDepartmentList(governmentType)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)).stream()
				.filter(data -> data.getId() == id).findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Cannot find master Department type for given id - " + departmentId));
	}

}
