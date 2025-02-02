package manage.store.service.session;

import manage.store.exception.InvalidParameterException;

/**
 * Session 저장 방식을 추상화한 인터페이스
 */
public interface UserSessionService {

    /**
     * 세션 키 생성
     * @return 세션 키
     */
    String createSessionKey();

    /**
     * 세션에 값 저장
     * @throws InvalidParameterException 세션 키가 비어있을 경우
     */
    void setSession(String key, Object value);

    /**
     * 세션에 저장된 값 가져오기
     * @return object: 세션에 저장된 값이 null이 아닐 때 <br>
     * null: 세션에 저장된 값이 null일 경우
     * @throws InvalidParameterException 세션 키가 비어있을 경우
     */
    Object getSession(String key);

    /**
     * 세션에 저장된 값 삭제
     * @throws InvalidParameterException 세션 키가 비어있을 경우
     */
    void removeSession(String key);

    /**
     * 세션에 저장된 값이 존재하는지 확인
     * @return true: 세션에 저장된 값이 null이 아닐 때 <br>
     * false: 세션에 저장된 값이 null일 경우
     * @throws InvalidParameterException 세션 키가 비어있을 경우
     */
    boolean isExist(String key);

}
