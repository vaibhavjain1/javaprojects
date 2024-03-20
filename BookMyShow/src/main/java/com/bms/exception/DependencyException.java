/**
 * 
 */
package com.bms.exception;

import lombok.Getter;

@Getter
public class DependencyException extends RuntimeException {

	private static final long serialVersionUID = 646182706219385282L;

	private final String message;

	public DependencyException(String message) {
		super(message);
		this.message = message;
	}

}