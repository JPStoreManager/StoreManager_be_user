package manage.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import manage.store.DTO.converter.LoginDtoMapper;
import manage.store.DTO.request.LoginRequest;
import manage.store.DTO.response.LoginResponse;
import manage.store.DTO.service.LoginResultDTO;
import manage.store.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class LoginController {

    private final UserAccountService userAccountService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResultDTO result = userAccountService.login(LoginDtoMapper.INSTANCE.reqToDto(loginRequest));

        return new ResponseEntity<>(new LoginResponse(result.isSuccess()), HttpStatus.OK);
    }

}
