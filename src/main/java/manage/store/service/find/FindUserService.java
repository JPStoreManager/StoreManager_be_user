package manage.store.service.find;

import manage.store.DTO.common.BaseResponse;
import manage.store.DTO.find.FindPwSendOtpRequest;
import manage.store.DTO.find.FindPwUpdatePwRequest;
import manage.store.DTO.find.FindPwValidateOtpRequest;
import manage.store.exception.InvalidParameterException;

public interface FindUserService {

    /** 비밀번호 찾기 */
    /**
     * 비밀번호 찾기 Step1 (Start) <br>
     * 사용자가 입력한 id와 email을 통해 계정의 존재를 조사하고 <br>
     * 계정이 존재한다면 OTP 전송. 실패 시 오류 메세지 반환
     * @param request userId {@code String, mandatory} 사용자 아이디 <br>
     *                userEmail {@code String, mandatory} 사용자 이메일
     * @return result {@code SuccessFlag} - 계정 인증 성공 시 Y, 계정 인증 실패 / 메일 전송 실패 시 N <br>
     * msg {@code String} - 성공 / 실패에 대한 메세지
     * @throws InvalidParameterException 사용자가 입력한 id와 email이 존재하지 않거나 유효하지 않을 경우
     */
    BaseResponse sendOtp(FindPwSendOtpRequest request);

    /**
     * 비밀번호 찾기 Step2 <br>
     * 시스템이 전송한 OTP 번호와 사용자가 입력한 OTP 번호가 동일한지 확인 <br>
     * @param request userId {@code String, mandatory} 사용자 아이디 <br>
     *                userEmail {@code String, mandatory} 사용자 이메일 <br>
     *                enteredOtp {@code String, mandatory} 사용자가 입력한 OTP 번호
     * @return result {@code SuccessFlag} - OTP 검증 성공 시 Y, 실패 시 N <br>
     * msg {@code String} - 성공 / 실패에 대한 메세지
     * @throws InvalidParameterException 사용자가 입력한 id와 email이 존재하지 않거나 유효하지 않을 경우
     */
    BaseResponse validateOtp(FindPwValidateOtpRequest request);

    /**
     * 비밀번호 찾기 Step3 (End) <br>
     * 신규 비밀번호에 대해 검증한 후 비밀번호를 사용자 입력한 신규 비밀번호로 업데이트 <br>
     * 비밀번호 찾기 완료 단계로, OTP 번호를 null로 초기화
     * @param request userId {@code String, mandatory} 사용자 아이디 <br>
     *                userEmail {@code String, mandatory} 사용자 이메일 <br>
     *                changedPwd {@code String, mandatory} 사용자가 신규로 입력한 비밀번호
     * @return result {@code SuccessFlag} - 비밀번호 업데이트 성공 시 Y, 잘못된 비밀번호일 시 시 N <br>
     * msg {@code String} - 성공 / 실패에 대한 메세지
     * @throws InvalidParameterException parameter validation에서 실패한 경우<br>
     *                                   - 사용자가 입력한 id와 email이 존재하지 않거나 유효하지 않을 경우 <br>
     *                                   - 사용자가 입력한 신규 비밀번호가 유효하지 않을 경우
     */
    BaseResponse updatePassword(FindPwUpdatePwRequest request);

}
