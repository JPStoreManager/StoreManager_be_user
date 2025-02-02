package manage.store.integration.find;

import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import manage.store.DTO.entity.User;
import manage.store.DTO.find.FindPwSendOtpRequest;
import manage.store.DTO.find.FindPwSendOtpResponse;
import manage.store.DTO.find.FindPwValidateOtpRequest;
import manage.store.DTO.find.FindUserPwSession;
import manage.store.consts.Profiles;
import manage.store.consts.SuccessFlag;
import manage.store.consts.Tags;
import manage.store.data.UserData;
import manage.store.integration.BaseIntegration;
import manage.store.repository.mapper.UserAccountMapper;
import manage.store.utils.ApiPathUtils;
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

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag(Tags.Test.INTEGRATION)
@Transactional
@Testcontainers
@SpringBootTest
@ActiveProfiles(Profiles.TEST)
@ExtendWith({RestDocumentationExtension.class})
public class FindPwValidateOtpTest extends BaseIntegration {

    private static final String SEND_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_SEND_OTP);
    private static final String VALIDATE_OTP_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.FIND_PW_VALIDATE_OTP);

    @Autowired
    private UserAccountMapper mapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private User user;

    /** Docker container for Test */
    @Container
    private static final DockerComposeContainer composeContainer = getDockerComposeContainer();
    {
        composeContainer.start();
    }

    @BeforeEach
    public void setup(TestInfo testInfo, RestDocumentationContextProvider restDocument) {
        mockMvc = getMockMvc(testInfo, context, restDocument);

        user = UserData.user1();
        mapper.insertUser(user);
    }


    /** validateOtp */
    @Test
    @Tag(Tags.Test.DOCS)
    @DisplayName("validateOtp 성공")
    public void validateOtp_success() throws Exception {
        // Given
        // 1) sendOtp
        final FindPwSendOtpRequest sendOtpRequest = new FindPwSendOtpRequest();
        sendOtpRequest.setUserId(user.getId());
        sendOtpRequest.setEmail(user.getEmail());

        // 1차로 otp 전송 단계로 통과 검증을 위한 세션 발급받기
        Gson gson = new Gson();
        MvcResult sendOtpResult = mockMvc.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(sendOtpRequest))).andReturn();

        FindPwSendOtpResponse findPwSendOtpResponse = gson.fromJson(sendOtpResult.getResponse().getContentAsString(), FindPwSendOtpResponse.class);

        // 2) validateOtp
        String otpNo = mapper.selectUserById(user.getId()).getOtpNo();
        final String sessionId = findPwSendOtpResponse.getSessionId();

        final FindPwValidateOtpRequest validateOtpRequest = new FindPwValidateOtpRequest();
        validateOtpRequest.setUserId(user.getId());
        validateOtpRequest.setEmail(user.getEmail());
        validateOtpRequest.setOtp(otpNo);

        final MockHttpSession session = new MockHttpSession();
        session.setAttribute(sessionId, new FindUserPwSession(FindUserPwSession.Step.SEND_OTP, user.getId(), user.getEmail()));

        ResultActions result = mockMvc.perform(
                post(VALIDATE_OTP_PATH)
                        .session(session)
                        .header("JP_FPW_ID", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(validateOtpRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()))
                .andExpect(jsonPath("$.sessionId").value(sessionId));
        addDocs(result);
    }

    @Test
    @DisplayName("validateOtp 실패 - 잘못된 파라미터")
    public void validateOtp_fail_invalidParameter() throws Exception {
        // Given
        final String otp = "123456";
        final String[][] params = {
                {null, null, null},

                {user.getId(), null, otp},
                {user.getId(), "", otp},
                {user.getId(), " ", otp},

                {null, user.getEmail(), otp},
                {"", user.getEmail(), otp},
                {" ", user.getEmail(), otp},

                {user.getId(), user.getEmail(), null},
                {user.getId(), user.getEmail(), ""},
                {user.getId(), user.getEmail(), " "},
        };

        // When - Then
        for(String[] param : params) {
            final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
            request.setUserId(param[0]);
            request.setEmail(param[1]);
            request.setOtp(param[2]);

            Gson gson = new Gson();
            mockMvc.perform(post(VALIDATE_OTP_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
        }
    }

    @Test
    @DisplayName("validateOtp 실패 - 잘못된 접근")
    public void validateOtp_fail_invalidAccess() throws Exception {
        // Given
        final FindPwValidateOtpRequest request = new FindPwValidateOtpRequest();
        request.setUserId(user.getId());
        request.setEmail(user.getEmail());
        request.setOtp("123456");

        // When
        Gson gson = new Gson();
        mockMvc.perform(
                post(VALIDATE_OTP_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("validateOtp 실패 - OTP 검증 실패")
    public void validateOtp_fail_invalidOtp() throws Exception {
        // Given
        final FindPwSendOtpRequest sendOtpRequest = new FindPwSendOtpRequest();
        sendOtpRequest.setUserId(user.getId());
        sendOtpRequest.setEmail(user.getEmail());

        // 1차로 otp 전송 단계로 통과 검증을 위한 세션 발급받기
        Gson gson = new Gson();
        MvcResult sendOtpResult = mockMvc.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(sendOtpRequest))).andReturn();

        FindPwSendOtpResponse findPwSendOtpResponse = gson.fromJson(sendOtpResult.getResponse().getContentAsString(), FindPwSendOtpResponse.class);

        // 2) validateOtp
        final String sessionId = findPwSendOtpResponse.getSessionId();

        final FindPwValidateOtpRequest validateOtpRequest = new FindPwValidateOtpRequest();
        validateOtpRequest.setUserId(user.getId());
        validateOtpRequest.setEmail(user.getEmail());
        String dbOtp = mapper.selectUserById(user.getId()).getOtpNo();
        validateOtpRequest.setOtp(dbOtp + "1");

        final MockHttpSession session = new MockHttpSession();
        session.setAttribute(sessionId, new FindUserPwSession(FindUserPwSession.Step.SEND_OTP, user.getId(), user.getEmail()));

        ResultActions result = mockMvc.perform(
                post(VALIDATE_OTP_PATH)
                        .session(session)
                        .header("JP_FPW_ID", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(validateOtpRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }


    @Override
    protected void addDocs(ResultActions result) throws Exception {
        result.andDo(document("findPassword/validateOtp",
                requestHeaders(
                        headerWithName("JP_FPW_ID").description("비밀번호 찾기 세션 ID")
                ),
                requestFields(
                        fieldWithPath("userId").description("사용자 아이디"),
                        fieldWithPath("email").description("사용자 이메일"),
                        fieldWithPath("otp").description("사용자가 입력한 OTP 번호")

                ),
                responseFields(
                        fieldWithPath("result").description("OTP 검증 성공 여부"),
                        fieldWithPath("msg").description("성공 / 실패에 대한 메세지"),
                        fieldWithPath("sessionId").description("비밀번호 찾기 세션 ID")
                )));
    }
}
