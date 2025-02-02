package manage.store.repository;

import manage.store.DTO.entity.User;
import manage.store.exception.InvalidParameterException;
import org.apache.ibatis.annotations.Param;

public interface UserAccountRepository {

    /**
     * 사용자 ID로 사용자 정보 조회
     * @param id 사용자 ID
     * @return User - 사용자 정보 <br>
     *         null - 사용자 정보가 없을 경우
     */
    User selectUserById(@Param(value = "id") String id);

    /**
     * 사용자 등록
     * @param user 사용자 정보
     * @return int - 등록된 사용자 수
     * @throws InvalidParameterException 사용자 validation에 실패한 경우
     */
    int insertUser(User user);

    /**
     * 사용자 정보 업데이트
     * @param user 사용자 정보
     * @return int - 등록된 사용자 수
     * @throws InvalidParameterException 사용자 validation에 실패한 경우
     */
    int updateUser(User user);

}
