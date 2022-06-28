package com.thalesmelo.reactivecassandra;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.thalesmelo.reactivecassandra.integrations.InvalidExternalServiceResponseException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
class RestExceptionHandler {

	@ExceptionHandler(InvalidExternalServiceResponseException.class)
	ResponseEntity integrationError(InvalidExternalServiceResponseException ex) {
		log.debug("handling exception::" + ex);
		return ResponseEntity.internalServerError().build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity badRequest(IllegalArgumentException ex) {
		log.debug("handling exception::" + ex);
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity InternalError(Exception ex) {
		log.debug("handling exception::" + ex);
		return ResponseEntity.internalServerError().build();
	}

}