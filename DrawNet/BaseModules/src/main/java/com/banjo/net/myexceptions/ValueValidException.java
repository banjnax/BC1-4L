package com.banjo.net.myexceptions;

public class ValueValidException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ValueValidException(String message, Throwable cause){
		super(message,cause);
	}
	public ValueValidException(String message){
		super(message);
	}
}
