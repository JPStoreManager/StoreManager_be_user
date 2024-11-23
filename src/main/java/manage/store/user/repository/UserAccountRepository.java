package manage.store.user.repository;

import manage.store.user.DTO.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountRepository {

    User selectUserById(@Param(value = "id") String id);

    int insertUser(User user);

    int updateUser(User user);

}
