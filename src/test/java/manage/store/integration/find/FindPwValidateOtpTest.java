package manage.store.integration.find;

import com.google.gson.Gson;
import manage.store.DTO.entity.User;
import manage.store.DTO.find.FindPwSendOtpRequest;
import manage.store.DTO.find.FindPwSendOtpResponse;
import manage.store.DTO.find.FindPwValidateOtpRequest;
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
        final FindPwSendOtpRequest sendOtpRequest = new FindPwSendOtpRequest();
        sendOtpRequest.setUserId(user.getId());
        sendOtpRequest.setEmail(user.getEmail());

        // 1차로 otp 전송 단계로 통과 검증을 위한 세션 발급받기
        Gson gson = new Gson();
        MvcResult sendOtpResult = mockMvc.perform(post(SEND_OTP_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(sendOtpRequest))).andReturn();
        FindPwSendOtpResponse findPwSendOtpResponse = gson.fromJson(sendOtpResult.getResponse().getContentAsString(), FindPwSendOtpResponse.class);

        final String sessionId = findPwSendOtpResponse.getSessionId();
        final FindPwValidateOtpRequest validateOtpRequest = new FindPwValidateOtpRequest();
        validateOtpRequest.setUserId(user.getId());
        validateOtpRequest.setEmail(user.getEmail());
        validateOtpRequest.setOtp(user.getOtpNo());

        ResultActions result = mockMvc.perform(post(SEND_OTP_PATH)
                        .header("JP_FPW_ID", sessionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(validateOtpRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()))
                .andExpect(jsonPath("$.sessionId").value(sessionId));
        addDocs(result);
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
