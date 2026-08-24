package com.kh.finalprj.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({
	ElementType.METHOD, 
	ElementType.TYPE, 
	ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@CommonsApiResponse
@ApiResponses({
	@ApiResponse(
		responseCode = "401",
		description = "인증되지 않았을 경우",
		content = @Content(
			mediaType = "text/plain",
			schema = @Schema(
				implementation = String.class,
				example = "Unauthorization"
			)
		)
	),
	@ApiResponse(
		responseCode = "403",
		description = "인증 권한이 부족한 경우",
		content = @Content(
			mediaType = "text/plain",
			schema = @Schema(
				implementation = String.class,
				example = "Forbidden"
			)
		)
	)
})
public @interface AuthApiResponse {
	
}
