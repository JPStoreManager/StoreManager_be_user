package manage.store.consts;

// TODO 메세지 임시 하드 코딩. 추후 DB화 필요
public interface Message {

    /** 작명 방법: {기능}_{SUCCESS/FAIL}_{상세내용} */

    /** 로그인 */
    String LOGIN_SUCCESS = "로그인 성공";
    String LOGIN_FAIL_INVALID_PARAM = "올바른 정보를 입력하세요.";
    String LOGIN_FAIL_NOT_EXIST_USER = "로그인 실패";
}
