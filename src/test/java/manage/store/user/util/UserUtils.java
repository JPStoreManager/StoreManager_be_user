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

    /**
     * 사용자 객체를 비교한다.
     * @param origin 원본 사용자 객체
     * @param compare 비교할 사용자 객체
     * @return 두 사용자 객체가 동일한지 여부
     * true{@code boolean} - origin과 compare가 동일한 경우
     * false{@code boolean} - origin과 compare가 동일하지 않은 경우
     */
    static boolean compareUser(User origin, User compare) {
        return origin.getId().equals(compare.getId())
                && origin.getPassword().equals(compare.getPassword())
                && origin.getName().equals(compare.getName())
                && origin.getResidentRegistNo().equals(compare.getResidentRegistNo())
                && origin.getPhoneNo().equals(compare.getPhoneNo())
                && origin.getEmail().equals(compare.getEmail())
                && origin.getAddress().equals(compare.getAddress())
                && origin.getAuthCd().equals(compare.getAuthCd())
                && origin.getWorkStartDate().equals(compare.getWorkStartDate())
                && origin.getWorkEndDate().equals(compare.getWorkEndDate())
                && origin.getWorkStatusCd().equals(compare.getWorkStatusCd())
                && origin.getBankName().equals(compare.getBankName())
                && origin.getBankAccountNo().equals(compare.getBankAccountNo())
                && origin.getMonthSalary().equals(compare.getMonthSalary())
                && origin.getHourWage().equals(compare.getHourWage())
                && origin.getCreatedBy().equals(compare.getCreatedBy())
                && origin.getLastUpdatedBy().equals(compare.getLastUpdatedBy());
    }

}
