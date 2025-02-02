package manage.store.service.login;

import jakarta.validation.Valid;
import manage.store.DTO.login.LoginRequest;
import manage.store.DTO.login.LoginResponse;
import manage.store.exception.InvalidParameterException;

public interface LoginService {
    /**
     * 사용자의 아이디와 비밀번호를 받아 로그인을 시도한다.
     * @param loginDTO 사용자의 로그인 정보를 담음 객체(id, password 필수)
     * @throws InvalidParameterException id 혹은 password가 빈 값일 경우
     * @return LoginResponse 로그인 결과 객체 <br>
     *  isSuccess {@code boolean}: 로그인 성공 여부 (true: 성공, false: 실패)
     *  message {@code string} (optional): 실패 시 에러 메시지
     */
    LoginResponse login(@Valid LoginRequest loginDTO);
}
