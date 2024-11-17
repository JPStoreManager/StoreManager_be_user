package manage.store.user.DTO.entity;

import lombok.*;

@Data
@NoArgsConstructor
public class User {
    private String id;
    private String password;
    private String name;
    private String residentRegistNo;
    private String phoneNo;
    private String email;
    private String address;
    private String authCd;
    private String workStartDate;
    private String workEndDate;
    private String workStatus;
    private String bankName;
    private String bankAccountNo;
    private String monthSalary;
    private String hourWage;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;
}
