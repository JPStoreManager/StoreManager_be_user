package manage.store.controller;

import com.google.gson.Gson;
import manage.store.DTO.entity.User;
import manage.store.DTO.login.LoginRequest;
import manage.store.DTO.login.LoginResponse;
import manage.store.consts.Message;
import manage.store.consts.SuccessFlag;
import manage.store.consts.Tags;
import manage.store.data.UserData;
import manage.store.service.login.LoginService;
import manage.store.testUtils.MockMvcUtils;
import manage.store.utils.ApiPathUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag(Tags.Test.UNIT)
@WebMvcTest(controllers = LoginController.class)
public class LoginControllerTest {

    private static final String LOGIN_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.LOGIN);

    private MockMvc mock;

    @MockBean
    private LoginService loginService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mock = MockMvcUtils.configureDefaultMockMvc(context).build();
    }

    @Test
    @DisplayName("login 성공")
    public void loginTest_success() throws Exception {
        // Given
        final User user = UserData.user1();
        final LoginRequest request = new LoginRequest(user.getId(), user.getPassword());

        given(loginService.login(any())).willReturn(new LoginResponse(SuccessFlag.Y));

        // When - then
        Gson gson = new Gson();
        mock.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()));
    }

    @Test
    @DisplayName("login 실패 - 잘못된 사용자 아이디 / 비밀번호")
    public void loginTest_fail_invalidParameter() throws Exception {
        // Given
        final User user = UserData.user1();
        final LoginRequest[] loginRequests = {
                new LoginRequest(" ", user.getPassword()),
                new LoginRequest("", user.getPassword()),
                new LoginRequest(null, user.getPassword()),

                new LoginRequest(user.getId(), " "),
                new LoginRequest(user.getId(), ""),
                new LoginRequest(user.getId(), null)
        };

        // When - then
        Gson gson = new Gson();
        for (LoginRequest request : loginRequests) {
            mock.perform(post(LOGIN_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }
    }

    @Test
    @DisplayName("login 실패 - 존재하지 않는 사용자 / 비밀번호 불일치")
    public void loginTest_fail_notValidParameter() throws Exception {
        // Given
        final User user = UserData.user1();
        final LoginRequest request = new LoginRequest(user.getId(), user.getPassword());

        given(loginService.login(any())).willReturn(new LoginResponse(SuccessFlag.N));

        // When - then
        Gson gson = new Gson();
        mock.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("login 실패 - 이름 없음")
    public void loginTest_fail_noName() throws Exception {
        // Given
        given(loginService.login(any())).willReturn(new LoginResponse(SuccessFlag.N));

        // When - then
        MvcResult result = mock.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"password123\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String bodyString = result.getResponse().getContentAsString();
        Assertions.assertThat(bodyString).isEqualTo(Message.LOGIN_FAIL_INVALID_PARAM);
    }

    @Test
    @DisplayName("login 실패 - 비밀번호 없음")
    public void loginTest_fail_noPassword() throws Exception {
        // Given
        given(loginService.login(any())).willReturn(new LoginResponse(SuccessFlag.N));

        // When - then
        MvcResult result = mock.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"userId1\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String bodyString = result.getResponse().getContentAsString();
        Assertions.assertThat(bodyString).isEqualTo(Message.LOGIN_FAIL_INVALID_PARAM);
    }
}
