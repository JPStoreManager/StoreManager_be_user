package manage.store.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import manage.store.DTO.entity.User;
import manage.store.exception.InvalidParameterException;
import manage.store.repository.mapper.UserAccountMapper;
import manage.store.utils.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountMapper userAccountMapper;

    @Override
    public User selectUserById(String id) {
        return userAccountMapper.selectUserById(id);
    }

    @Override
    public int insertUser(User user) {
        if(!isUserValid(user)) throw new InvalidParameterException("User is invalid. " + user);

        try {
            return userAccountMapper.insertUser(user);
        } catch (DataIntegrityViolationException e) {
            log.info("Fail to insert user. User: {}, Error message: {}", user, ExceptionUtils.getExceptionErrorMsg(e));
            return 0;
        }
    }

    @Override
    public int updateUser(User user) {
        if(!isUserValid(user)) throw new InvalidParameterException("User is invalid. " + user);

        try {
            return userAccountMapper.updateUser(user);
        } catch (DataIntegrityViolationException e) {
            log.info("Fail to update user. User: {}, Error message: {}", user, ExceptionUtils.getExceptionErrorMsg(e));
            return 0;
        }
    }

    private boolean isUserValid(User user) {
        return user != null && user.isValid();
    }
}
