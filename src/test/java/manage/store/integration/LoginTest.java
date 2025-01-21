package manage.store.integration;

import com.google.gson.Gson;
import manage.store.DTO.entity.User;
import manage.store.DTO.login.LoginRequest;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
@ExtendWith({RestDocumentationExtension.class})
public class LoginTest extends BaseIntegration {

    private static final String LOGIN_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.LOGIN);

    /**
     * Docker container for Test
     */
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
    @DisplayName("login 성공")
    public void loginTest_success() throws Exception {
        // Given
        final LoginRequest request = new LoginRequest();
        request.setId(user.getId());
        request.setPassword("password1");

        Gson gson = new Gson();

        // When - Then
        ResultActions result = mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.Y.getValue()));

        addDocs(result);
    }

    @Test
    @DisplayName("login 실패 - 존재하지 않는 아이디")
    public void loginTest_fail_notExistUser() throws Exception {
        // Given
        final LoginRequest request = new LoginRequest();
        request.setId("NotExistUserId");
        request.setPassword("password1");

        Gson gson = new Gson();
        // When - Then
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("login 실패 - 비밀번호 불일치")
    public void loginTest_fail_passwordNotMatch() throws Exception {
        // Given
        final LoginRequest request = new LoginRequest();
        request.setId(user.getId());
        request.setPassword("password2");

        Gson gson = new Gson();

        // When - Then
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(SuccessFlag.N.getValue()));
    }

    @Override
    protected void addDocs(ResultActions result) throws Exception {
        result.andDo(document("login",
            requestFields(
                fieldWithPath("id").type(JsonFieldType.STRING).description("사용자 아이디"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호")
            ),
            responseFields(
                fieldWithPath("result").type(JsonFieldType.STRING).description("로그인 성공 여부"),
                fieldWithPath("msg").type(JsonFieldType.STRING).description("로그인 결과 메시지")
            )
        ));
    }

}
