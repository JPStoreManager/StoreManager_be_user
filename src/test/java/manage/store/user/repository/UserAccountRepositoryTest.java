package manage.store.user.repository;

import manage.store.user.DTO.entity.User;
import manage.store.user.util.UserUtils;
import manage.store.user.servlet.UserApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ContextConfiguration(classes = UserApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    private static User[] users;

    @BeforeEach
    public void setUp() {
        final String idPrefix = "userId";

        users = new User[3];
        for (int i = 1; i <= users.length; i++) {
            User user = UserUtils.createUser(idPrefix + i);
            userAccountRepository.insertUser(user);
            users[i-1] = user;
        }
    }

    @Test
    @DisplayName("insertUser 성공")
    public void insertUser_success() {
        // given
        User user = UserUtils.createUser("userId4");

        // when
        int result = userAccountRepository.insertUser(user);

        // then
        assertEquals(1, result);

        User dbUser = userAccountRepository.selectUserById(user.getId());
        assertTrue(UserUtils.compareUser(user, dbUser));
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 id")
    public void insertUser_fail_duplicateId() {
        // given
        User user = UserUtils.createUser("userId1");

        // when
        int result = userAccountRepository.insertUser(user);

        // then
        assertEquals(0, result);
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 email")
    public void insertUser_fail_duplicateEmail() {
        // given
        User user = UserUtils.createUser("userId4");
        user.setEmail(users[0].getEmail());

        // when
        int result = userAccountRepository.insertUser(user);

        // then
        assertEquals(0, result);
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 phoneNo")
    public void insertUser_fail_duplicatePhoneNo() {
        // given
        User user = UserUtils.createUser("userId4");
        user.setPhoneNo(users[0].getPhoneNo());

        // when
        int result = userAccountRepository.insertUser(user);

        // then
        assertEquals(0, result);
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 residentRegistNo")
    public void insertUser_fail_duplicateResidentRegistNo() {
        // given
        User user = UserUtils.createUser("userId4");
        user.setResidentRegistNo(users[0].getResidentRegistNo());

        // when
        int result = userAccountRepository.insertUser(user);

        // then
        assertEquals(0, result);
    }



    @Test
    void selectUserById() {
        // given
        User user = users[0];

        // when
        User dbUser = userAccountRepository.selectUserById(user.getId());

        // then
        assertTrue(UserUtils.compareUser(user, dbUser));
    }
}