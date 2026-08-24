package com.kh.finalprj.error;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice(annotations = {RestController.class})//이렇게 해서 잘 안됐어서 아래거 썼었음
@RestControllerAdvice(basePackages = {"com.kh.finalprj.controller"})
public class ErrorRestController {
	
	@ExceptionHandler(TargetNotfoundException.class)
	public ResponseEntity<String> notFound() {
//		return ResponseEntity<String>.notFound().build();
		return ResponseEntity.status(404).body("Target not found");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> serverError(Exception e) {
		e.printStackTrace();
		return ResponseEntity.status(500).body("Server Error");
	}
	
	@ExceptionHandler(value = {
			WhoAreYouException.class,
			JwtValidationException.class
	})
	public ResponseEntity<String> whoAreYou(){
		return ResponseEntity.status(401).body("not authorized");
	}
	
	@ExceptionHandler(value = {
			GetOutException.class
	})
	public ResponseEntity<String> getOut(){
		return ResponseEntity.status(403).body("need permission");
	}
	
	@ExceptionHandler(value = {
			MethodArgumentNotValidException.class
	})
	public ResponseEntity<String> badRequest() {
		return ResponseEntity.status(400).body("requirement mismatch");
	}

}

