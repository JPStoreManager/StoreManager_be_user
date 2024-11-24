package manage.store.DTO.entity;

import lombok.*;

import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
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
    private String workStatusCd;
    private String bankName;
    private String bankAccountNo;
    private Integer monthSalary;
    private Integer hourWage;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId())
                && Objects.equals(getPassword(), user.getPassword())
                && Objects.equals(getName(), user.getName())
                && Objects.equals(getResidentRegistNo(), user.getResidentRegistNo())
                && Objects.equals(getPhoneNo(), user.getPhoneNo())
                && Objects.equals(getEmail(), user.getEmail())
                && Objects.equals(getAddress(), user.getAddress())
                && Objects.equals(getAuthCd(), user.getAuthCd())
                && Objects.equals(getWorkStartDate(), user.getWorkStartDate())
                && Objects.equals(getWorkEndDate(), user.getWorkEndDate())
                && Objects.equals(getWorkStatusCd(), user.getWorkStatusCd())
                && Objects.equals(getBankName(), user.getBankName())
                && Objects.equals(getBankAccountNo(), user.getBankAccountNo())
                && Objects.equals(getMonthSalary(), user.getMonthSalary())
                && Objects.equals(getHourWage(), user.getHourWage());
    }
}


