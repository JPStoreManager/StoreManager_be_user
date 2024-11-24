package manage.store.service;

import manage.store.DTO.service.LoginDTO;
import manage.store.DTO.service.LoginResultDTO;
import manage.store.exception.InvalidParameterException;

public interface UserAccountService {
    /**
     * 사용자의 아이디와 비밀번호를 받아 로그인을 시도한다.
     * @param loginDTO 사용자의 로그인 정보를 담음 객체(id, password 필수)
     * @throws InvalidParameterException id 혹은 password가 빈 값일 경우
     * @return LoginResultDTO 로그인 결과 객체 <br>
     *  isSuccess {@code boolean}: 로그인 성공 여부 (true: 성공, false: 실패)
     *  message {@code string} (optional): 실패 시 에러 메시지
     */
    LoginResultDTO login(LoginDTO loginDTO);
}
