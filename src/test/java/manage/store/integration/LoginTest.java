package manage.store.integration;

import com.google.gson.Gson;
import manage.store.DTO.login.LoginRequest;
import manage.store.config.WebConfiguration;
import manage.store.consts.SuccessFlag;
import manage.store.consts.Tags;
import manage.store.controller.LoginController;
import manage.store.data.UserData;
import manage.store.repository.mapper.UserAccountMapper;
import manage.store.service.login.LoginServiceImpl;
import manage.store.servlet.UserApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag(Tags.Test.INTEGRATION)
@Transactional
@SpringBootTest(classes = UserApplication.class)
@Import(WebConfiguration.class)
@ContextConfiguration(classes = { WebConfiguration.class})
public class LoginTest {

    public static final String LOGIN_PATH = "/login";

    private MockMvc mockMvc;

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(loginService))
                .build();

        userAccountMapper.insertUser(UserData.user1);

        for (String beanDefinitionName : applicationContext.getBeanDefinitionNames()) {
//            System.out.println(beanDefinitionName);
        }
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
        mockMvc.perform(post(LOGIN_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loginResult").value(SuccessFlag.Y.getValue()));
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

}
