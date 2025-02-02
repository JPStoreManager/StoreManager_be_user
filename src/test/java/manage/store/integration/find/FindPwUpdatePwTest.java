package manage.store.integration.find;

import com.google.gson.Gson;
import manage.store.DTO.entity.User;
import manage.store.DTO.find.*;
import manage.store.consts.Profiles;
import manage.store.consts.Tags;
import manage.store.data.UserData;
import manage.store.integration.BaseIntegration;
import manage.store.repository.mapper.UserAccountMapper;
import manage.store.utils.ApiPathUtils;
import manage.store.utils.SecretUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag(Tags.Test.INTEGRATION)
@Testcontainers
@Transactional
@SpringBootTest
@ActiveProfiles(Profiles.TEST)
@ExtendWith(RestDocumentationExtension.class)
public class FindPwUpdatePwTest extends BaseIntegration {

    private static final String SEND_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_SEND_OTP);
    private static final String VALIDATE_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_VALIDATE_OTP);
    private static final String UPDATE_PW_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_UPDATE_PW);

    @Container
    private static final DockerComposeContainer container = getDockerComposeContainer();
    {
        container.start();
    }

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserAccountMapper userAccountMapper;

    private MockMvc mock;

    private User user;

    @BeforeEach
    public void setUp(TestInfo testInfo, RestDocumentationContextProvider provider) {
        mock = getMockMvc(testInfo, context, provider);

        user = UserData.user1();
        userAccountMapper.insertUser(user);
    }

    @Test
    @Tag(Tags.Test.DOCS)
    @DisplayName("updatePassword 성공")
    public void updatePassword_success() throws Exception {
        // given
        // 1) sendOtp
        final FindPwSendOtpRequest sendOtpRequest = new FindPwSendOtpRequest();
        sendOtpRequest.setUserId(user.getId());
        sendOtpRequest.setEmail(user.getEmail());

        final Gson gson = new Gson();
        MvcResult sendOtpResult = mock.perform(post(SEND_OTP_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(sendOtpRequest)))
                .andReturn();

        FindPwSendOtpResponse sendOtpResponse = gson.fromJson(sendOtpResult.getResponse().getContentAsString(), FindPwSendOtpResponse.class);

        // 2) validateOtp
        // Skip 가능

        // 3) updatePassword
        final String sendOtpSessionId = sendOtpResponse.getSessionId();

        final FindPwUpdatePwRequest updatePwRequest = new FindPwUpdatePwRequest();
        updatePwRequest.setUserId(user.getId());
        updatePwRequest.setEmail(user.getEmail());
        updatePwRequest.setNewPassword("1q2w3e4r");

        final MockHttpSession session = new MockHttpSession();
        session.setAttribute(sendOtpSessionId, new FindUserPwSession(FindUserPwSession.Step.VALIDATE_OTP, user.getId(), user.getEmail()));

        // when
        ResultActions result = mock.perform(post(UPDATE_PW_PATH)
                        .session(session)
                        .header("JP_FPW_ID", sendOtpSessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updatePwRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Y"));

        // then
        String password = userAccountMapper.selectUserById(user.getId()).getPassword();
        assertThat(SecretUtils.verify("1q2w3e4r", password)).isTrue();

        addDocs(result);
    }

    @Test
    @DisplayName("updatePassword 실패 - 잘못된 파라미터")
    public void updatePassword_fail_invalidParameter() throws Exception {
        // given
        final String[][] params = {
                {null, null, null},

                {user.getId(), user.getEmail(), null},
                {user.getId(), user.getEmail(), ""},
                {user.getId(), user.getEmail(), " "},

                {user.getId(), user.getEmail(), "12345678"},
                {user.getId(), user.getEmail(), "qwerasdf"},
                {user.getId(), user.getEmail(), "1234!@#$"},
                {user.getId(), user.getEmail(), "qwer!@#$"},
                {user.getId(), user.getEmail(), "1234qwe"},

                {user.getId(), null, "1q2w3e4r"},
                {user.getId(), "", "1q2w3e4r"},
                {user.getId(), " ", "1q2w3e4r"},

                {null, user.getEmail(), "1q2w3e4r"},
                {"", user.getEmail(), "1q2w3e4r"},
                {" ", user.getEmail(), "1q2w3e4r"},
        };

        // When - Then
        for(String[] param : params) {
            final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);
            request.setNewPassword(param[2]);

            Gson gson = new Gson();
            mock.perform(post(UPDATE_PW_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.result").value("N"));
        }
    }

    @Test
    @DisplayName("updatePassword 실패 - 잘못된 접근(no 이전 step)")
    public void updatePassword_fail_invalidAccess() throws Exception {
        // given
        final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
        request.setUserId(user.getId());
        request.setEmail(user.getEmail());
        request.setNewPassword("1q2w3e4r");

        // When
        Gson gson = new Gson();
        mock.perform(post(UPDATE_PW_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("N"));
    }

    @Test
    @DisplayName("updatePassword 실패 - 잘못된 접근(sendOtp step까지)")
    public void updatePassword_fail_invalidAccess_sendOtp() throws Exception {
        // given
        // 1) sendOtp
        final FindPwSendOtpRequest sendOtpRequest = new FindPwSendOtpRequest();
        sendOtpRequest.setUserId(user.getId());
        sendOtpRequest.setEmail(user.getEmail());

        final Gson gson = new Gson();
        MvcResult sendOtpResult = mock.perform(post(SEND_OTP_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(sendOtpRequest)))
                .andReturn();

        // 2) updatePw
        FindPwSendOtpResponse sendOtpResponse = gson.fromJson(sendOtpResult.getResponse().getContentAsString(), FindPwSendOtpResponse.class);
        final String sendOtpSessionId = sendOtpResponse.getSessionId();

        final FindPwUpdatePwRequest request = new FindPwUpdatePwRequest();
        request.setUserId(user.getId());
        request.setEmail(user.getEmail());
        request.setNewPassword("1q2w3e4r");

        // When
        mock.perform(post(UPDATE_PW_PATH)
                .header("JP_FPW_ID", sendOtpSessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value("N"));
    }

    @Override
    protected void addDocs(ResultActions result) throws Exception {

        result.andDo(document("findPassword/updatePassword",
                requestHeaders(
                        headerWithName("JP_FPW_ID").description("비밀번호 찾기 세션 아이디")
                ),
                requestFields(
                        fieldWithPath("userId").description("사용자 아이디"),
                        fieldWithPath("email").description("사용자 이메일"),
                        fieldWithPath("newPassword").description("사용자가 신규로 입력한 비밀번호")
                ),
                responseFields(
                        fieldWithPath("result").description("비밀번호 업데이트 성공 여부"),
                        fieldWithPath("msg").description("성공 / 실패에 대한 메세지")
                )));
    }
}
