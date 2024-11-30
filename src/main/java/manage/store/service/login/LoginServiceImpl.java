package manage.store.service.login;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import manage.store.DTO.entity.User;
import manage.store.DTO.login.LoginRequest;
import manage.store.DTO.login.LoginResponse;
import manage.store.consts.SuccessFlag;
import manage.store.repository.UserAccountRepository;
import manage.store.utils.SecretUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public LoginResponse login(@Valid LoginRequest loginDTO) {
        // 1. 아이디를 통해 사용자 계정 조회
        // 만약 없으면 실패 return
        User user = userAccountRepository.selectUserById(loginDTO.getId());
        if(user == null) return new LoginResponse(SuccessFlag.N);

        // 2. 전달된 비밀번호가 암호화된 비밀번호와 일치하는지 확인
        if(SecretUtils.verify(loginDTO.getPassword(), user.getPassword())) return new LoginResponse(SuccessFlag.Y);
        else return new LoginResponse(SuccessFlag.N);
    }

}
