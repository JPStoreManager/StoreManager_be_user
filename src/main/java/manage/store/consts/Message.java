package manage.store.consts;

// TODO 메세지 임시 하드 코딩. 추후 DB화 필요
public interface Message {

    /** 작명 방법: {기능}_{SUCCESS/FAIL}_{상세내용} */

    /** 로그인 */
    String LOGIN_SUCCESS = "로그인 성공";
    String LOGIN_FAIL_INVALID_PARAM = "올바른 정보를 입력하세요.";
    String LOGIN_FAIL_NOT_EXIST_USER = "로그인 실패";

    /** 비밀번호 찾기 */
    String FIND_PW_FAIL_INVALID_PARAM = "올바른 정보를 입력하지 않았거나 잘못된 접근입니다.";
    String FIND_PW_SEND_OTP_SUCCESS = "OTP 전송 성공";
    String FIND_PW_SEND_OTP_FAIL_FAIL_TO_SEND_OTP = "OTP 메일 전송에 실패하였습니다.";
    String FIND_PW_VALIDATE_OTP_SUCCESS = "OTP 인증 성공";
    String FIND_PW_VALIDATE_OTP_FAIL_NOT_VALID = "OTP 인증 실패";
    String FIND_PW_UPDATE_PW_SUCCESS = "비밀번호 업데이트 성공";
    String FIND_PW_UPDATE_PW_FAIL_INVALID_PW = "올바르지 않는 비밀번호입니다.";
}
