package manage.store.repository.mapper;

import manage.store.DTO.entity.User;
import manage.store.config.DBConfiguration;
import manage.store.consts.Tags;
import manage.store.testUtils.UserUtils;
import org.junit.jupiter.api.*;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

@Tag(Tags.Test.UNIT)
@Testcontainers
@MybatisTest
@ContextConfiguration(classes = DBConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAccountMapperTest {

    /** Docker container for Test */
    @Container
    private static final DockerComposeContainer composeContainer = new DockerComposeContainer(new File("./docker-compose.yml"));
    static {
        composeContainer.start();
    }

    @Autowired
    private UserAccountMapper userAccountMapper;

    private static User[] users;

    @BeforeEach
    public void setUp() {
        final String idPrefix = "userId";

        users = new User[3];
        for (int i = 1; i <= users.length; i++) {
            User user = UserUtils.createUser(idPrefix + i);
            userAccountMapper.insertUser(user);
            users[i-1] = user;
        }
    }

    /** select */
    @Test
    @Order(1)
    @DisplayName("selectUserById 성공")
    void selectUserById_success() {
        // Given
        User user = users[0];

        // When
        User dbUser = userAccountMapper.selectUserById(user.getId());

        // Then
        assertTrue(user.equals(dbUser));
    }

    @Test
    @Order(1)
    @DisplayName("selectUserById 실패 - user")
    void selectUserById_fail_noUser() {
        // Given
        String noUserId = "noUserId";

        // When
        User dbUser = userAccountMapper.selectUserById(noUserId);

        // Then
        assertThat(dbUser).isNull();
    }


    /** insert */
    @Test
    @DisplayName("insertUser 성공")
    public void insertUser_success() {
        // Given
        User user = UserUtils.createUser("userId4");

        // When
        int result = userAccountMapper.insertUser(user);

        // Then
        assertEquals(1, result);
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 id(PK)")
    public void insertUser_fail_duplicateId_PK() {
        // Given
        User user = UserUtils.createUser("userId1");

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 email(UNIQUE)")
    public void insertUser_fail_duplicateEmail_UNIQUE() {
        // Given
        User user = UserUtils.createUser("userId4");
        user.setEmail(users[0].getEmail());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
        assertThat(userAccountMapper.selectUserById(user.getId())).isNull();
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 phoneNo(UNIQUE)")
    public void insertUser_fail_duplicatePhoneNo_UNIQUE() {
        // Given
        User user = UserUtils.createUser("userId4");
        user.setPhoneNo(users[0].getPhoneNo());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
        assertThat(userAccountMapper.selectUserById(user.getId())).isNull();
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 residentRegistNo(UNIQUE)")
    public void insertUser_fail_duplicateResidentRegistNo_UNIQUE() {
        // Given
        User user = UserUtils.createUser("userId4");
        user.setResidentRegistNo(users[0].getResidentRegistNo());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
        assertThat(userAccountMapper.selectUserById(user.getId())).isNull();
    }

    /** update */
    @Test
    @DisplayName("updateUser 성공")
    public void updateUser_success() {
        // Given
        User user = UserUtils.createUser(users[0].getId());

        // When
        int result = userAccountMapper.updateUser(user);

        // Then
        assertEquals(1, result);
        assertTrue(user.equals(userAccountMapper.selectUserById(user.getId())));
    }

    @Test
    @DisplayName("updateUser 실패 - 존재하지 않는 id")
    public void updateUser_fail_noUser() {
        // Given
        User user = UserUtils.createUser("noUserId");

        // When
        int result = userAccountMapper.updateUser(user);

        // Then
        assertEquals(0, result);
    }

    @Test
    @DisplayName("updateUser 실패 - 중복된 email(UNIQUE)")
    public void updateUser_fail_duplicateEmail_UNIQUE() {
        // Given
        User user = users[0];
        user.setEmail(users[1].getEmail());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.updateUser(user));
    }

    @Test
    @DisplayName("updateUser 실패 - 중복된 phoneNo(UNIQUE)")
    public void updateUser_fail_duplicatePhoneNo_UNIQUE() {
        // Given
        User user = users[0];
        user.setPhoneNo(users[1].getPhoneNo());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.updateUser(user));
    }

    @Test
    @DisplayName("updateUser 실패 - 중복된 residentRegistNo(UNIQUE)")
    public void updateUser_fail_duplicateResidentRegistNo_UNIQUE() {
        // Given
        User user = users[0];
        user.setResidentRegistNo(users[1].getResidentRegistNo());

        // When - Then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.updateUser(user));
    }
}