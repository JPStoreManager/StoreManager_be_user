package manage.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.DTO.common.BaseResponse;
import manage.store.DTO.common.ParameterValidationFailResponse;
import manage.store.DTO.find.FindPwSendOtpRequest;
import manage.store.DTO.find.FindPwSendOtpResponse;
import manage.store.DTO.find.FindUserPwSession;
import manage.store.consts.Message;
import manage.store.exception.InvalidParameterException;
import manage.store.service.find.FindUserService;
import manage.store.service.session.FindUserPwSessionService;
import manage.store.utils.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/find")
@RestController
@RequiredArgsConstructor
public class FindUserController implements BaseController {

    private final FindUserService findUserService;

    private final FindUserPwSessionService findUserPwSessionService;

    /**
     * 비밀번호 찾기 - OTP 발송 <br>
     * @param request userId {@code String, mandatory}: 사용자 아이디 <br>
     *                email {@code String, mandatory}: 사용자 이메일 <br>
     * @return result SuccessFlag - 계정 인증 성공: Y <br>
     * 계정 인증 실패 / 메일 전송 실패: N <br>
     * msg String - 성공 / 실패에 대한 메세지 <br>
     * @throws InvalidParameterException 사용자가 입력한 id와 email이 존재하지 않거나 유효하지 않을 경우
     */
    @PostMapping("pw/sendOtp")
    public ResponseEntity<FindPwSendOtpResponse> sendOtp(@RequestBody @Valid FindPwSendOtpRequest request) {
        BaseResponse result = findUserService.sendOtp(request);

        if(result.getResult().isSuccess()) {
            String sessionKey = findUserPwSessionService.createSessionKey();
            findUserPwSessionService.updateSession(sessionKey, request, FindUserPwSession.Step.OTP);

            return ResponseEntity.ok(new FindPwSendOtpResponse(result.getResult(), result.getMsg(), sessionKey));
        }

        return ResponseEntity.ok(new FindPwSendOtpResponse(result.getResult(), result.getMsg()));
    }

    @ExceptionHandler({InvalidParameterException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<BaseResponse> handleInvalidParameterException(Exception e) {
        log.info("Invalid Parameter was inputted. Error Message: {}", ExceptionUtils.getExceptionErrorMsg(e));

        return ResponseEntity.badRequest().body(new ParameterValidationFailResponse(Message.FIND_PW_FAIL_INVALID_PARAM));
    }

}
