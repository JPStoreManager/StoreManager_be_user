package manage.store.controller.find;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.DTO.common.BaseResponse;
import manage.store.DTO.common.ParameterValidationFailResponse;
import manage.store.DTO.find.*;
import manage.store.consts.Message;
import manage.store.consts.SuccessFlag;
import manage.store.controller.BaseController;
import manage.store.exception.InvalidParameterException;
import manage.store.service.find.FindUserPwService;
import manage.store.service.session.FindUserPwSessionService;
import manage.store.utils.ExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/find/pw")
@RestController
@RequiredArgsConstructor
public class FindUserPwController implements BaseController {

    private static final String FIND_PW_HEADER_ID = "JP_FPW_ID";

    private final FindUserPwService findUserPwService;

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
    @PostMapping("sendOtp")
    public ResponseEntity<FindPwSendOtpResponse> sendOtp(@RequestBody @Valid FindPwSendOtpRequest request) {
        BaseResponse result = findUserPwService.sendOtp(request);

        if(result.getResult().isSuccess()) {
            String sessionKey = findUserPwSessionService.createSessionKey();
            findUserPwSessionService.updateSession(sessionKey, request, FindUserPwSession.Step.SEND_OTP);

            return ResponseEntity.ok(new FindPwSendOtpResponse(result.getResult(), result.getMsg(), sessionKey));
        }

        return ResponseEntity.ok(new FindPwSendOtpResponse(result.getResult(), result.getMsg()));
    }

    /**
     * 비밀번호 찾기 - OTP 검증 <br>
     * @param request userId {@code String, mandatory}: 사용자 아이디 <br>
     *                email {@code String, mandatory}: 사용자 이메일 <br>
     *                otp {@code String, mandatory}: 사용자가 입력한 OTP 번호
     * @return result {@code SuccessFlag} OTP 검증 성공 여부 <br>
     * - OTP 검증 성공: Y <br>
     * - OTP 검증 실패: N <br>
     * msg{@code String} - 성공 / 실패에 대한 메세지 <br>
     */
    @PostMapping("validateOtp")
    public ResponseEntity<FindPwValidateOtpResponse> validateOtp(@RequestHeader(value = FIND_PW_HEADER_ID) String sessionId,
                                                                 @RequestBody @Valid FindPwValidateOtpRequest request) {
        // 1. otp 전송 단계를 거쳤는지 session을 통해 검증
        FindUserPwSession session = findUserPwSessionService.getSession(sessionId);
        if(!findUserPwService.isValidStep(session, FindUserPwSession.Step.VALIDATE_OTP)) {
            return ResponseEntity.badRequest().body(new FindPwValidateOtpResponse(SuccessFlag.N, Message.FIND_PW_VALIDATE_OTP_FAIL_NOT_VALID));
        }

        // 2. otp 검증
        BaseResponse result = findUserPwService.validateOtp(request);
        if(result.getResult().isSuccess()) {
            // 3. otp 검증 성공 시 session 업데이트
            findUserPwSessionService.updateSession(sessionId, request, FindUserPwSession.Step.VALIDATE_OTP);

            return ResponseEntity.ok(new FindPwValidateOtpResponse(result.getResult(), result.getMsg(), sessionId));
        }

        // 4. 응답 반환
        return ResponseEntity.ok(new FindPwValidateOtpResponse(result.getResult(), result.getMsg()));
    }

    /**
     * 각 API의 바디 파라미터 검증 실패 시 처리 로직
     */
    @ExceptionHandler({InvalidParameterException.class, MethodArgumentNotValidException.class, MissingRequestHeaderException.class})
    public ResponseEntity<BaseResponse> handleInvalidParameterException(Exception e) {
        log.info("Invalid Parameter was inputted. Error Message: {}", ExceptionUtils.getExceptionErrorMsg(e));

        return ResponseEntity.badRequest().body(new ParameterValidationFailResponse(Message.FIND_PW_FAIL_INVALID_PARAM));
    }

}
