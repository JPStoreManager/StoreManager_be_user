package manage.store.controller;

import manage.store.DTO.login.LoginResponse;
import manage.store.consts.Message;
import manage.store.consts.SuccessFlag;
import manage.store.consts.Tags;
import manage.store.service.login.LoginService;
import manage.store.servlet.UserApplication;
import manage.store.testUtils.MockMvcUtils;
import manage.store.utils.ApiPathUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag(Tags.Test.UNIT)
@WebMvcTest(controllers = LoginController.class)
@ContextConfiguration(classes = UserApplication.class)
public class LoginControllerTest {

    private static final String LOGIN_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.LOGIN);

    private MockMvc mock;

    @MockBean
    private LoginService loginService;

    @MockBean
    private DataSource datasource;

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
        given(loginService.login(any())).willReturn(new LoginResponse(SuccessFlag.Y));

        // When - then
         mock.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"userId1\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginResult").value(SuccessFlag.Y.getValue()));
    }

    @Test
    @DisplayName("login 실패 - 존재하지 않는 사용자 / 비밀번호 불일치")
    public void loginTest_fail_notValidParameter() throws Exception {
        // Given
        given(loginService.login(any())).willReturn(new LoginResponse(SuccessFlag.N));

        // When - then
        mock.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"userId1\", \"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginResult").value(SuccessFlag.N.getValue()));
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
