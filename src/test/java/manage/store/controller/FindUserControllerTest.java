package manage.store.controller;

import com.google.gson.Gson;
import manage.store.DTO.common.BaseResponse;
import manage.store.DTO.entity.User;
import manage.store.DTO.find.FindPwSendOtpRequest;
import manage.store.DTO.find.FindUserPwSession;
import manage.store.consts.Message;
import manage.store.consts.SuccessFlag;
import manage.store.consts.Tags;
import manage.store.data.UserData;
import manage.store.exception.InvalidParameterException;
import manage.store.service.find.FindUserService;
import manage.store.service.session.FindUserPwSessionService;
import manage.store.testUtils.MockMvcUtils;
import manage.store.utils.ApiPathUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag(Tags.Test.UNIT)
@WebMvcTest(controllers = FindUserController.class)
class FindUserControllerTest {

    private MockMvc mock;

    @MockBean
    private FindUserService findUserService;

    @MockBean
    private FindUserPwSessionService findUserPwSessionService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        mock = MockMvcUtils.configureDefaultMockMvc(context).build();
    }

    /**
     * sendOtp
     */
    private static final String SEND_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_SEND_OTP);

    @Test
    @DisplayName("sendOtp 성공")
    void sendOtp_success() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(user.getId());
        request.setEmail(user.getEmail());
        final String SESSION_ID = "sessionKey";

        given(findUserService.sendOtp(any()))
                .willReturn(new BaseResponse(SuccessFlag.Y, Message.FIND_PW_SEND_OTP_SUCCESS));

        given(findUserPwSessionService.createSessionKey()).willReturn(SESSION_ID);

        doNothing().when(findUserPwSessionService).updateSession(SESSION_ID, request, FindUserPwSession.Step.OTP);

        // When - Then
        Gson gson = new Gson();
        mock.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()))
                .andExpect(jsonPath("$.msg").value(Message.FIND_PW_SEND_OTP_SUCCESS))
                .andExpect(jsonPath("$.sessionId").value(SESSION_ID));
    }

    @Test
    @DisplayName("sendOtp 실패 - 유효하지 않은 파리미터")
    void sendOtp_fail_invalidParameters() throws Exception {
        // Given
        final User user = UserData.user1();
        final String[][] params = {
                {null, null},

                {user.getId(), null},
                {user.getId(), ""},
                {user.getId(), "  "},

                {null, user.getEmail()},
                {"", user.getEmail()},
                {"  ", user.getEmail()},

                {user.getId(), "wrongEmail1"},
                {user.getId(), "wro@a"},
                {user.getId(), "wro@.c"},
                {user.getId(), "@abc.com"},
        };

        // When - Then
        Gson gson = new Gson();
        for (String[] param : params) {
            FindPwSendOtpRequest request = new FindPwSendOtpRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);

            mock.perform(post(SEND_OTP_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(gson.toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
        }
    }

    @Test
    @DisplayName("sendOtp 실패 - 존재하지 않는 계정")
    void sendOtp_fail_notExistUser() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(user.getId());
        request.setEmail(user.getEmail());

        given(findUserService.sendOtp(any())).willThrow(new InvalidParameterException());

        // When - Then
        Gson gson = new Gson();
        mock.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("sendOtp 실패 - 메일 전송 실패")
    void sendOtp_fail_sendMailFail() throws Exception {
        // Given
        final User user = UserData.user1();
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(user.getId());
        request.setEmail(user.getEmail());

        given(findUserService.sendOtp(any()))
                .willReturn(new BaseResponse(SuccessFlag.N, Message.FIND_PW_SEND_OTP_FAIL_FAIL_TO_SEND_OTP));

        // When - Then
        Gson gson = new Gson();
        mock.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

}