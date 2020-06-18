package com.revature.exceptions;

public class NoMoneyException extends RuntimeException {
	private static final long serialVersionUID = 2962237309937221593L;

	@Override
	public String toString() {
		return "IllegalBalanceException []";
	}

	public NoMoneyException() {
		super();
	}

	public NoMoneyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoMoneyException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoMoneyException(String message) {
		super(message);
	}

	public NoMoneyException(Throwable cause) {
		super(cause);
	}

}

