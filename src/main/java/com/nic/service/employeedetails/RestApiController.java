package com.nic.service.employeedetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Validated
@RestController("nicEmployeeDetailsRestApiController")
@RequestMapping("/api/")
public class RestApiController {

	EmployeeDetailsService service;

	final String empCodeOnlyNosType = "Employee Code should be of number type";

	public RestApiController(EmployeeDetailsService service) {
		this.service = service;
	}

	@ExceptionHandler({ ResponseStatusException.class })
	public ResponseEntity<Map<String, List<String>>> handle(ResponseStatusException ex) {
		List<String> errors = new ArrayList<>(1);
		errors.add(ex.getReason());
		Map<String, List<String>> errorMap = new HashMap<>(1);
		errorMap.put("errors", errors);

		return new ResponseEntity<>(errorMap, ex.getStatus());
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	public ResponseEntity<Map<String, List<String>>> handle(IllegalArgumentException e) {
		List<String> errors = new ArrayList<>(1);
		errors.add(e.getMessage());
		Map<String, List<String>> errorMap = new HashMap<>(1);
		errorMap.put("errors", errors);

		return ResponseEntity.badRequest().body(errorMap);
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Map<String, List<String>>> handle(ConstraintViolationException e) {
		List<String> errors = new ArrayList<>();
		e.getConstraintViolations().forEach(violation -> errors.add(violation.getMessage()));

		Map<String, List<String>> errorMap = new HashMap<>(1);
		errorMap.put("errors", errors);
		return ResponseEntity.badRequest().body(errorMap);
	}

	@GetMapping("nic/{employeeCode}/groups")
	public List<GroupModel> fetchListOfNicGroupsWithHogEmployeeCode(
			@PathVariable @Pattern(regexp = "\\d+", message = empCodeOnlyNosType) String employeeCode) {
		Integer empCode = Integer.parseInt(employeeCode);
		return service.getGroupList(empCode);
	}

	@GetMapping("nic/{employeeCode}/groups/{groupId}")
	public GroupModel fetchNicGroupWithHoGEmployeeCode(
			@PathVariable @Pattern(regexp = "\\d+", message = empCodeOnlyNosType) String employeeCode,
			@PathVariable String groupId) {
		Integer empCode = Integer.parseInt(employeeCode);
		return service.getGroupList(empCode).stream().filter(data -> data.getId().equals(groupId)).findFirst()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Cannot find Group Name for given Group Id"));
	}

	@GetMapping("nic/{employeeCode}/personaldetails")
	public EmployeeDetail fetchNicEmployeeDetails(
			@PathVariable @Pattern(regexp = "\\d+", message = empCodeOnlyNosType) String employeeCode,
			@RequestParam @Pattern(regexp = "hog|hod|other-one|other-two", message = "Request 'type' should be either 'hog', 'hod', 'other-one' or 'other-two'") String type) {
		Integer empCode = Integer.parseInt(employeeCode);
		return service.getEmployeeDetails(empCode, type);
	}

}
