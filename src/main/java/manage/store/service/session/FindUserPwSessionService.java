package manage.store.service.session;

import lombok.RequiredArgsConstructor;
import manage.store.DTO.find.FindPwBaseRequest;
import manage.store.DTO.find.FindUserPwSession;
import manage.store.exception.InvalidParameterException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class FindUserPwSessionService {

    private final UserSessionService userSessionService;

    public String createSessionKey() {
        return userSessionService.createSessionKey();
    }

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
