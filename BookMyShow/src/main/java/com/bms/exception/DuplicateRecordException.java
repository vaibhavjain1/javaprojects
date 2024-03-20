/**
 * 
 */
package com.bms.exception;

import lombok.Getter;

@Getter
public class DuplicateRecordException extends RuntimeException {

	private static final long serialVersionUID = 646182706219385282L;

	private final String message;

	public DuplicateRecordException(String message) {
		super(message);
		this.message = message;
	}

}