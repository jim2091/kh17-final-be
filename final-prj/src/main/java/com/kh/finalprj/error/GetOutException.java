package com.kh.finalprj.error;

//회원이 상위권한 기능에 접근하면 발생할 예외
public class GetOutException extends RuntimeException{//이 예외는 처리를 생략할 수 있다

	public GetOutException() {
		super();
	}

	public GetOutException(String message) {
		super(message);
	}
	
}
