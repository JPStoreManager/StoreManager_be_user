package manage.store.repository;

import manage.store.DTO.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserAccountRepository {

    User selectUserById(@Param(value = "id") String id);

    int insertUser(User user);

    int updateUser(User user);

}
