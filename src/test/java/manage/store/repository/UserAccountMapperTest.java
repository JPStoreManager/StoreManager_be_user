package manage.store.repository;

import manage.store.user.DTO.entity.User;
import manage.store.user.repository.mapper.UserAccountMapper;
import manage.store.util.UserUtils;
import manage.store.user.servlet.UserApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

@MybatisTest
@ContextConfiguration(classes = UserApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAccountMapperTest {

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
    @DisplayName("select 성공")
    void selectUserById_success() {
        // given
        User user = users[0];

        // when
        User dbUser = userAccountMapper.selectUserById(user.getId());

        // then
        assertTrue(user.equals(dbUser));
    }

    @Test
    @Order(1)
    @DisplayName("select 실패")
    void selectUserById_fail_noUser() {
        // given
        String noUserId = "noUserId";

        // when
        User dbUser = userAccountMapper.selectUserById(noUserId);

        // then
        assertThat(dbUser).isNull();
    }


    /** insert */
    @Test
    @DisplayName("insertUser 성공")
    public void insertUser_success() {
        // given
        User user = UserUtils.createUser("userId4");

        // when
        int result = userAccountMapper.insertUser(user);

        // then
        assertEquals(1, result);
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 id(PK)")
    public void insertUser_fail_duplicateId_PK() {
        // given
        User user = UserUtils.createUser("userId1");

        // when - then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 email(UNIQUE)")
    public void insertUser_fail_duplicateEmail_UNIQUE() {
        // given
        User user = UserUtils.createUser("userId4");
        user.setEmail(users[0].getEmail());

        // when - then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
        assertThat(userAccountMapper.selectUserById(user.getId())).isNull();
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 phoneNo(UNIQUE)")
    public void insertUser_fail_duplicatePhoneNo_UNIQUE() {
        // given
        User user = UserUtils.createUser("userId4");
        user.setPhoneNo(users[0].getPhoneNo());

        // when - then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
        assertThat(userAccountMapper.selectUserById(user.getId())).isNull();
    }

    @Test
    @DisplayName("insertUser 실패 - 중복된 residentRegistNo(UNIQUE)")
    public void insertUser_fail_duplicateResidentRegistNo_UNIQUE() {
        // given
        User user = UserUtils.createUser("userId4");
        user.setResidentRegistNo(users[0].getResidentRegistNo());

        // when - then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.insertUser(user));
        assertThat(userAccountMapper.selectUserById(user.getId())).isNull();
    }

    /** update */
    @Test
    @DisplayName("updateUser 성공")
    public void updateUser_success() {
        // given
        User user = UserUtils.createUser(users[0].getId());

        // when
        int result = userAccountMapper.updateUser(user);

        // then
        assertEquals(1, result);
        assertTrue(user.equals(userAccountMapper.selectUserById(user.getId())));
    }

    @Test
    @DisplayName("updateUser 실패 - 존재하지 않는 id")
    public void updateUser_fail_noUser() {
        // given
        User user = UserUtils.createUser("noUserId");

        // when
        int result = userAccountMapper.updateUser(user);

        // then
        assertEquals(0, result);
    }

    @Test
    @DisplayName("updateUser 실패 - 중복된 email(UNIQUE)")
    public void updateUser_fail_duplicateEmail_UNIQUE() {
        // given
        User user = users[0];
        user.setEmail(users[1].getEmail());

        // when - then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.updateUser(user));
    }

    @Test
    @DisplayName("updateUser 실패 - 중복된 phoneNo(UNIQUE)")
    public void updateUser_fail_duplicatePhoneNo_UNIQUE() {
        // given
        User user = users[0];
        user.setPhoneNo(users[1].getPhoneNo());

        // when - then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.updateUser(user));
    }

    @Test
    @DisplayName("updateUser 실패 - 중복된 residentRegistNo(UNIQUE)")
    public void updateUser_fail_duplicateResidentRegistNo_UNIQUE() {
        // given
        User user = users[0];
        user.setResidentRegistNo(users[1].getResidentRegistNo());

        // when - then
        assertThrows(DuplicateKeyException.class, () -> userAccountMapper.updateUser(user));
    }
}