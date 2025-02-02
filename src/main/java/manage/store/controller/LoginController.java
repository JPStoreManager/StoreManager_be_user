package manage.store.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.DTO.login.LoginRequest;
import manage.store.DTO.login.LoginResponse;
import manage.store.consts.Message;
import manage.store.service.login.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController implements BaseController{

    private final LoginService loginService;

    /**
     * 로그인
     * @param loginRequest 사용자 계정 정보 <br>
     *                     id {@code String, Mandatory}: 사용자 아이디 <br>
     *                     password {@code String, Mandatory}: 사용자 비밀번호 <br>
     * @return LoginResponse 로그인 결과 <br>
     *                       result {@code SuccessFlag}: 로그인 성공 여부 <br>
     *                       msg {@code String}: 로그인 결과 메시지
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse result = loginService.login(loginRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Parameter의 유효성 검증에 실패한 Request 처리
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<String> handleInvalidParameterRequest(Exception e) {
        log.info("Invalid Parameter was inputted. Error Message: {}", e.getMessage());

        return ResponseEntity.badRequest().body(Message.LOGIN_FAIL_INVALID_PARAM);
    }

}
