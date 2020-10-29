package com.nic.service.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@ExceptionHandler({ ConstraintViolationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String[]> handleError(ConstraintViolationException e) {
		List<String> errors = new ArrayList<>();
		e.getConstraintViolations().forEach(v -> errors.add(v.getMessage()));

		Map<String, String[]> errorMap = new HashMap<>(1);
		errorMap.put("errors", errors.toArray(new String[errors.size()]));
		return errorMap;
	}

	@ExceptionHandler({ RuntimeException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, String[]> handleError(RuntimeException ex) {
		log.error("Runtime Exception occured {}", ex);

		Map<String, String[]> errorMap = new HashMap<>(1);
		errorMap.put("errors", new String[] { "Internal server exception occured. Please retry after a while" });
		return errorMap;
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String[]> handleError(IllegalArgumentException ex) {
		Map<String, String[]> errorMap = new HashMap<>(1);
		errorMap.put("errors", new String[] { ex.getMessage() });
		return errorMap;
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class })
	public ResponseEntity<Map<String, String[]>> handle(HttpMessageNotReadableException ex) {
		Map<String, String[]> errorMap = new HashMap<>(1);
		if (ex.getMessage().contains("Required request body is missing")) {
			errorMap.put("errors", new String[] { "Request body is required/missing" });
			return new ResponseEntity<Map<String, String[]>>(errorMap, HttpStatus.BAD_REQUEST);
		}

		errorMap.put("errors", new String[] { "Unexpected exception occured!" });
		return new ResponseEntity<Map<String, String[]>>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({ ResponseStatusException.class })
	public ResponseEntity<Map<String, String[]>> handleError(ResponseStatusException ex) {
		Map<String, String[]> errorMap = new HashMap<>(1);
		errorMap.put("errors", new String[] { ex.getReason() });
		return new ResponseEntity<Map<String, String[]>>(errorMap, ex.getStatus());
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Map<String, String[]>> handleError(MethodArgumentNotValidException ex) {
		Map<String, String[]> map = new HashMap<>(ex.getBindingResult().getFieldErrorCount());

		for (FieldError field : ex.getBindingResult().getFieldErrors()) {
			List<FieldError> errorList = ex.getBindingResult().getFieldErrors(field.getField());
			List<String> errors = new ArrayList<>(errorList.size());
			errorList.forEach(error -> errors.add(error.getDefaultMessage()));
			map.put(field.getField(), errors.toArray(new String[errors.size()]));
		}

		Map<String, Map<String, String[]>> errorMap = new HashMap<>(1);
		errorMap.put("errors", map);
		return errorMap;
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String[]> handleError(MissingServletRequestParameterException ex) {
		Map<String, String[]> errorMap = new HashMap<>(1);
		errorMap.put("errors", new String[] { ex.getMessage() });
		return errorMap;
	}

	@ExceptionHandler({ InvalidDataAccessApiUsageException.class })
	public ResponseEntity<Map<String, String[]>> handlerError(InvalidDataAccessApiUsageException ex) {
		Map<String, String[]> errorMap = new HashMap<>(1);

		if (ExceptionUtils.indexOfThrowable(ex, IllegalArgumentException.class) != -1) {
			System.out.println("output " + ExceptionUtils.getRootCause(ex).getMessage());
			if (StringUtils.containsIgnoreCase(ExceptionUtils.getRootCause(ex).getMessage(), "invalid application")) {
				errorMap.put("errors", new String[] { "Application number is invalid!" });
				return ResponseEntity.badRequest().body(errorMap);
			}
		}

		errorMap.put("errors", new String[] { "Unknown exception occured!" });
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
	}

}
