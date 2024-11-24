package manage.store.repository.mapper;

import manage.store.DTO.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountMapper {

    User selectUserById(@Param(value = "id") String id);

    int insertUser(User user);

    int updateUser(User user);

}
