package manage.store.integration;

import com.google.gson.Gson;
import manage.store.DTO.entity.User;
import manage.store.DTO.find.FindPwSendOtpRequest;
import manage.store.consts.Profiles;
import manage.store.consts.SuccessFlag;
import manage.store.consts.Tags;
import manage.store.data.UserData;
import manage.store.repository.UserAccountRepository;
import manage.store.utils.ApiPathUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag(Tags.Test.INTEGRATION)
@Transactional
@Testcontainers
@SpringBootTest
@ActiveProfiles(Profiles.TEST)
@ExtendWith({RestDocumentationExtension.class})
public class FindPwSendOtpTest extends BaseIntegration {

    private static final String SEND_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_SEND_OTP);

    /** Docker container for Test */
    @Container
    private static final DockerComposeContainer composeContainer = getDockerComposeContainer();
    static {
        composeContainer.start();
    }

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private final User user = UserData.user1();

    @BeforeEach
    public void setUp(TestInfo testInfo, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = getMockMvc(testInfo, context, restDocumentation);

        userAccountRepository.insertUser(user);
    }

    @Test
    @Tag(Tags.Test.DOCS)
    @DisplayName("sendOtp 성공")
    public void sendOtp_success() throws Exception {
        // Given
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(user.getId());
        request.setEmail(user.getEmail());

        // When - Then
        Gson gson = new Gson();
        ResultActions result = mockMvc.perform(post(SEND_OTP_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()));

        User updatedUser = userAccountRepository.selectUserById(request.getUserId());
        assertThat(updatedUser.getOtp()).isNotEqualTo(user.getOtp());

        addDocs(result);
    }

    @Test
    @DisplayName("sendOtp 실패 - 잘못된 파라미터")
    public void sendOtp_fail_invalidParameter() throws Exception {
        // Given
        final String[][] params = {
                {null, null},
                {user.getId(), null},
                {user.getId(), ""},
                {user.getId(), " "},

                {null, user.getEmail()},
                {"", user.getEmail()},
                {" ", user.getEmail()},

                {user.getId(), "wrongEmail"},
                {user.getId(), "wrongEmail@"},
                {user.getId(), "wrongEmail.com"},
                {user.getId(), "wrongEmail@.com"},
                {user.getId(), "@com"},
        };

        // When - Then
        for(String[] param : params) {
            final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);

            Gson gson = new Gson();
            mockMvc.perform(post(SEND_OTP_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
        }
    }

    @Test
    @DisplayName("sendOtp 실패 - 존재하지 않는 계정")
    public void sendOtp_fail_notExistUser() throws Exception {
        // Given
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId("notExistUser");
        request.setEmail(user.getEmail());

        // When - Then
        Gson gson = new Gson();
        mockMvc.perform(post(SEND_OTP_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("sendOtp 실패 - 계정은 존재하지만 일치하지 않는 이메일")
    public void sendOtp_fail_notMatchEmail() throws Exception {
        // Given
        final FindPwSendOtpRequest request = new FindPwSendOtpRequest();
        request.setUserId(user.getId());
        request.setEmail("notMatchEmail@a.com");

        // When - Then
        Gson gson = new Gson();
        mockMvc.perform(post(SEND_OTP_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Override
    protected void addDocs(ResultActions result) throws Exception {
        result.andDo(document("findPassword/sendOtp",
                requestFields(
                        fieldWithPath("userId").description("사용자 아이디").type(String.class),
                        fieldWithPath("email").description("사용자 이메일").type(String.class)
                ),
                responseFields(
                        fieldWithPath("result").description("계정 확인 및 otp 전송 성공 여부").type(String.class),
                        fieldWithPath("msg").description("otp 전송 성공/실패 메세지").type(String.class),
                        fieldWithPath("sessionId").description("otp 전송에 성공하여 다음 단계가 진행될 때 인증에 사용할 토큰이 저장될 key").type(String.class).optional()
                )));
    }

}
