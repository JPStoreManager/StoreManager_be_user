package manage.store.integration;

import com.google.gson.Gson;
import manage.store.DTO.login.LoginRequest;
import manage.store.consts.SuccessFlag;
import manage.store.consts.Tags;
import manage.store.data.UserData;
import manage.store.repository.mapper.UserAccountMapper;
import manage.store.servlet.UserApplication;
import manage.store.testUtils.MockMvcUtils;
import manage.store.utils.ApiPathUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag(Tags.Test.INTEGRATION)
@Transactional
@SpringBootTest(classes = UserApplication.class)
@ExtendWith({RestDocumentationExtension.class})
public class LoginTest extends BaseIntegration {

    private static final String LOGIN_PATH = ApiPathUtils.getPath(ApiPathUtils.ApiName.LOGIN);

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;


    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = setMockMvc(context, restDocumentation);

        userAccountMapper.insertUser(UserData.user1);
    }


    @Test
    @DisplayName("login 성공")
    public void loginTest_success() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setId("testerId1");
        request.setPassword("password1");

        Gson gson = new Gson();

        // When - Then
        ResultActions result = mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginResult").value(SuccessFlag.Y.getValue()));

        addDocs(result);
    }

    @Test
    @DisplayName("login 실패 - 존재하지 않는 아이디")
    public void loginTest_fail_notExistUser() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setId("NotExistUserId");
        request.setPassword("password1");

        Gson gson = new Gson();
        // When - Then
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginResult").value(SuccessFlag.N.getValue()));
    }

    @Test
    @DisplayName("login 실패 - 비밀번호 불일치")
    public void loginTest_fail_passwordNotMatch() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setId("testerId1");
        request.setPassword("password2");

        Gson gson = new Gson();

        // When - Then
        mockMvc.perform(post(LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginResult").value(SuccessFlag.N.getValue()));
    }

    @Override
    protected void addDocs(ResultActions result) throws Exception {
        ConstrainedFields reqFields = new ConstrainedFields(LoginRequest.class);

        result.andDo(document("login",
            requestFields(
                fieldWithPath("id").type(JsonFieldType.STRING).description("사용자 아이디"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호")
            ),
            responseFields(
                fieldWithPath("loginResult").type(JsonFieldType.STRING).description("로그인 성공 여부"),
                fieldWithPath("msg").type(JsonFieldType.STRING).description("로그인 결과 메시지")
            )
        ));
    }

}
