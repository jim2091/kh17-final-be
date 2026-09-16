package com.kh.finalprj.error;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice(annotations = {RestController.class})
// 이렇게 해서 잘 안됐어서 아래거 사용
@RestControllerAdvice(basePackages = {"com.kh.finalprj.controller"})
public class ErrorRestController {

    /*
     * ==========================================
     * 404
     * ==========================================
     */
    @ExceptionHandler(TargetNotfoundException.class)
    public ResponseEntity<String> notFound() {
        return ResponseEntity
                .status(404)
                .body("Target not found");
    }


    /*
     * ==========================================
     * 401
     * ==========================================
     */
    @ExceptionHandler(value = {
            WhoAreYouException.class,
            JwtValidationException.class
    })
    public ResponseEntity<String> whoAreYou() {
        return ResponseEntity
                .status(401)
                .body("not authorized");
    }


    /*
     * ==========================================
     * 403
     * ==========================================
     */
    @ExceptionHandler(GetOutException.class)
    public ResponseEntity<String> getOut() {
        return ResponseEntity
                .status(403)
                .body("need permission");
    }


    /*
     * ==========================================
     * 400 - 잘못된 데이터
     * ==========================================
     *
     * WrongDataException에서 전달한 메시지를
     * 그대로 프론트엔드로 전달
     *
     * 예:
     * throw new WrongDataException(
     *     "이미 참여했거나 탈퇴한 프로젝트에는 다시 참여할 수 없습니다."
     * );
     *
     * ↓
     *
     * HTTP 400
     * body:
     * 이미 참여했거나 탈퇴한 프로젝트에는 다시 참여할 수 없습니다.
     */
    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<String> badRequest(WrongDataException e) {

        String message = e.getMessage();

        if (message == null || message.trim().isEmpty()) {
            message = "requirement mismatch";
        }

        return ResponseEntity
                .status(400)
                .body(message);
    }


    /*
     * ==========================================
     * 400 - Validation 오류
     * ==========================================
     *
     * @Valid, @NotNull, @NotBlank 등의 검증 오류
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validationError(
            MethodArgumentNotValidException e) {

        return ResponseEntity
                .status(400)
                .body("requirement mismatch");
    }


    /*
     * ==========================================
     * 500
     * ==========================================
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> serverError(Exception e) {

        e.printStackTrace();

        return ResponseEntity
                .status(500)
                .body("Server Error");
    }
}
