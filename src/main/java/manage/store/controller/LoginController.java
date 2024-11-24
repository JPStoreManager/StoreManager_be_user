package manage.store.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import manage.store.DTO.login.LoginRequest;
import manage.store.DTO.login.LoginResponse;
import manage.store.service.login.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse result = loginService.login(loginRequest);

        return new ResponseEntity<>(new LoginResponse(result.isSuccess()), HttpStatus.OK);
    }

    /**
     * Parameter의 유효성 검증에 실패한 Request 처리
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<String> handleInvalidParameterRequest(RuntimeException e) {
        return ResponseEntity.badRequest().body("올바른 정보를 입력하세요.");
    }

}
