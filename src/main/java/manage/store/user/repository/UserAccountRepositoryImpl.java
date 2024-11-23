package manage.store.user.repository;

import lombok.RequiredArgsConstructor;
import manage.store.user.DTO.entity.User;
import manage.store.user.repository.mapper.UserAccountMapper;
import org.springframework.stereotype.Repository;

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
        return userAccountMapper.insertUser(user);
    }

    @Override
    public int updateUser(User user) {
        return userAccountMapper.updateUser(user);
    }
}
