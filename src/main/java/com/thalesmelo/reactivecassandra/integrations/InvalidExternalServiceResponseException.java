package com.thalesmelo.reactivecassandra.integrations;

public class InvalidExternalServiceResponseException extends Exception {

	private static final long serialVersionUID = 6470060249280613556L;

	public InvalidExternalServiceResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidExternalServiceResponseException(String message) {
		super(message);
	}

}
