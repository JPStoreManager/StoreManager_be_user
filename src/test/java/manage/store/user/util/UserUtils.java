package manage.store.user.util;

import manage.store.user.DTO.entity.User;

public interface UserUtils {

    /**
     * 사용자 객체를 생성한다.
     * @param id 사용자 아이디
     * @return 사용자 객체
     */
    static User createUser(String id) {
        return User.builder().id(id).password(CommonUtils.getRandomString(20))
                .name(CommonUtils.getRandomString(3))
                .residentRegistNo(CommonUtils.getRandomString(13))
                .phoneNo(CommonUtils.getRandomString(11))
                .email(CommonUtils.getRandomString(20))
                .address(CommonUtils.getRandomString(100))
                .authCd(CommonUtils.getRandomString(3))
                .workStartDate(CommonUtils.getRandomDate())
                .workEndDate(CommonUtils.getRandomDate())
                .workStatusCd(CommonUtils.getRandomString(3))
                .bankName(CommonUtils.getRandomString(6))
                .bankAccountNo(CommonUtils.getRandomString(20))
                .monthSalary(CommonUtils.getRandomInt())
                .hourWage(CommonUtils.getRandomInt())
                .createdBy(id)
                .lastUpdatedBy(id)
                .build();
    }

}
