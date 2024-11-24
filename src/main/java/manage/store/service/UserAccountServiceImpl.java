package manage.store.service;

import lombok.RequiredArgsConstructor;
import manage.store.DTO.entity.User;
import manage.store.DTO.service.LoginDTO;
import manage.store.DTO.service.LoginResultDTO;
import manage.store.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public LoginResultDTO login(LoginDTO loginDTO) {
        // TODO 0. 인자 validation

        // 1. 아이디를 통해 사용자 계정 조회
        // 만약 없으면 실패 return
        User user = userAccountRepository.selectUserById(loginDTO.getId());
        if(user == null) return new LoginResultDTO(false);

        // 2. 현재 전달된 비밀번호의 암호화 및 사용자 계정의 비밀번호와 동일한지 검증
        // 만약 동일하지 않으면 실패 return, 같다면 성공 return
        String encryptedPassword = encryptPassword(loginDTO.getPassword());
        if(!user.getPassword().equals(encryptedPassword)) return new LoginResultDTO(false);
        else return new LoginResultDTO(true);
    }

    private String encryptPassword(String password) {
        // 비밀번호 암호화 로직

        return password;
    }

}
