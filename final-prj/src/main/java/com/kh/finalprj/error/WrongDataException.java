package com.kh.finalprj.error;

public class WrongDataException extends RuntimeException{

	public WrongDataException() {
		super();
	}

	public WrongDataException(String message) {
		super(message);
	}
	
}
