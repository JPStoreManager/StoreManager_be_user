package manage.store.service.session;

import lombok.RequiredArgsConstructor;
import manage.store.DTO.find.FindPwBaseRequest;
import manage.store.DTO.find.FindUserPwSession;
import manage.store.exception.InvalidParameterException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 비밀번호 찾기의 각 단계가 전단계를 거쳤는지 검증하기 위한 토큰을 관리하는 서비스 계층
 */
@Service
@RequiredArgsConstructor
public class FindUserPwSessionService {

    private final UserSessionService userSessionService;

    /**
     * 세션키를 통해 세션 조회
     * @param key 세션 키
     * @return FindUserPwSession - 세션 키가 존재, null - 존재하지 않음
     */
    public FindUserPwSession getSession(String key) {
        return (FindUserPwSession) userSessionService.getSession(key);
    }

    /**
     * 세션 키 생성
     * @return 세션 키
     */
    public String createSessionKey() {
        return userSessionService.createSessionKey();
    }

    /**
     * 최종 성공 단계를 세션에 저장
     * @param key 세션 키
     * @param request userId {@code String, mandatory}: 사용자 아이디 <br>
     *                email {@code String, mandatory}: 사용자 이메일
     * @param successStep 마지막 성공 단계
     */
    public void updateSession(String key, FindPwBaseRequest request, FindUserPwSession.Step successStep) {
        if(!StringUtils.hasText(key) ||
                (request == null || !StringUtils.hasText(request.getUserId()) || !StringUtils.hasText(request.getEmail())) ||
                successStep == null) throw new InvalidParameterException();

        FindUserPwSession session = null;
        // update session
        if (userSessionService.isExist(key)) {
            session = (FindUserPwSession) userSessionService.getSession(key);
            session.setCompletedStep(successStep);
        }
        // create session
        else {
            session = new FindUserPwSession(successStep, request.getUserId(), request.getEmail());
        }

        userSessionService.setSession(key, session);
    }

    public void removeSession(String key) {
        userSessionService.removeSession(key);
    }
    
}
