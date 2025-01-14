package manage.store.service.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import manage.store.exception.InvalidParameterException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class HttpSessionUserSessionService implements UserSessionService{

    private static final String SESSION_KEY = "findUserPw";

    @Override
    public String createSessionKey() {
        // httpSession은 이미 JSESSIONID라는 키로 세션을 생성하므로 별도의 키를 생성할 필요가 없다.
        return SESSION_KEY;
    }

    @Override
    public void setSession(String key, Object value) {
        if(!StringUtils.hasText(key))
            throw new InvalidParameterException("The parameter is invalid. key is empty.");

        HttpSession session = getSession();

        session.setAttribute(key, value);
    }

    @Override
    public Object getSession(String key) {
        if(!StringUtils.hasText(key))
            throw new InvalidParameterException("The parameter is invalid. key is empty.");

        HttpSession session = getSession();

        return session.getAttribute(key);
    }

    @Override
    public void removeSession(String key) {
        if(!StringUtils.hasText(key))
            throw new InvalidParameterException("The parameter is invalid. key is empty.");

        HttpSession session = getSession();

        session.removeAttribute(key);
    }

    @Override
    public boolean isExist(String key) {
        if(!StringUtils.hasText(key))
            throw new InvalidParameterException("The parameter is invalid. key is empty.");

        HttpSession session = getSession();

        return session.getAttribute(key) != null;
    }

    private HttpSession getSession() {
        // 현재 스레드에서 Request 정보를 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        // HttpServletRequest 객체 얻어오기
        HttpServletRequest request = attributes.getRequest();

        // 세션 얻기 (없으면 새로 생성)
        return request.getSession();
    }

}
