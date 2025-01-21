package manage.store.data;

import manage.store.DTO.entity.User;

/**
 * Test용 임의 데이터
 */
public class UserData {
    private static final String email = "JumpingParkManager@gmail.com";

    public static User user1() {
        return User.builder()
                .id("testerId1")
                .password("$2a$10$GSis.dob/tMyiVXui7UNDOI5hlcnvbL3ryW8kIaGT7D31FcKK2OI.")
                .name("테스트1")
                .residentRegistNo("1231231231231")
                .phoneNo("11111111111")
                .email(email)
                .address("test address 1")
                .authCd("AUTH1")
                .workStartDate("2024-01-01")
                .workEndDate(null)
                .workStatusCd("ING")
                .bankName("BANK1")
                .bankAccountNo("1231231231")
                .monthSalary(2000000)
                .hourWage(null)
                .otp("otp1")
                .createdBy("hs.lee0130")
                .lastUpdatedBy("hs.lee0130")
                .build();
    }

    public static User user2() {
        return User.builder()
                .id("testerId2")
                .password("$2a$10$qa90EbI.v1OxaE1cWc/pSu6HHkWPb6/LSwqo9aBx5mCnzJqwH115.")
                .name("테스트2")
                .residentRegistNo("1234123412341")
                .phoneNo("22222222222")
                .email(email)
                .address("test address 2")
                .authCd("AUTH2")
                .workStartDate("2024-02-01")
                .workEndDate("2024-12-31")
                .workStatusCd("END")
                .bankName("BANK2")
                .bankAccountNo("1234123412")
                .monthSalary(null)
                .hourWage(10000)
                .otp("otp2")
                .createdBy("hs.lee0130")
                .lastUpdatedBy("hs.lee0130")
                .build();
    }

    public static User user3() {
        return User.builder()
                .id("testerId3")
                .password("$2a$10$rCi9o8yyOGm7KzO2Q7kVH.fQShgIeEOTEMs.Md06LUynpqEq5oUYO")
                .name("테스트3")
                .residentRegistNo("123451234512")
                .phoneNo("33333333333")
                .email(email)
                .address("test address 3")
                .authCd("AUTH3")
                .workStartDate("2024-03-01")
                .workEndDate(null)
                .workStatusCd("ING")
                .bankName("BANK3")
                .bankAccountNo("12345123451")
                .monthSalary(12300000)
                .hourWage(null)
                .otp("otp3")
                .createdBy("hs.lee0130")
                .lastUpdatedBy("hs.lee0130")
                .build();
    }
}
