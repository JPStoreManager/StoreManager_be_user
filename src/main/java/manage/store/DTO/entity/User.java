package manage.store.DTO.entity;

import jakarta.validation.constraints.*;
import lombok.*;
import manage.store.annotation.Password;
import manage.store.annotation.UserEmail;
import manage.store.annotation.UserId;
import manage.store.consts.Const;

import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    @UserId
    private String id;

    @Size(max = 300, message = "password는 최대 300자리 이하입니다.")
    @Password
    private String password;

    @NotBlank(message = "name은 공란일 수 없습니다.")
    @Size(min = 2, max = 5, message = "name은 최소 2자리 이상 최대 5자리 미만입니다.")
    private String name;

    @Size(min = 13, max = 13, message = "residentRegistNo는 13자리입니다.")
    private String residentRegistNo;

    @Size(min = 11, max = 11, message = "phoneNo는 11자리 입니다.")
    private String phoneNo;

    @Size(max = 100, message = "email은 최대 100자 이하입니다.")
    @UserEmail
    private String email;

    @Size(max = 200, message = "address은 최대 200자리 이하입니다.")
    private String address;

    @NotBlank(message = "authCd는 공란일 수 없습니다.")
    @Size(min = 1, max = 5, message = "authCd는 최소 1자리 이상 최대 5자리 이하여야 합니다.")
    private String authCd;

    @NotBlank(message = "workStartDate는 공란일 수 없습니다.")
    private String workStartDate;

    private String workEndDate;

    @NotBlank(message = "workStatusCd는 공란일 수 없습니다.")
    @Size(min = 1, max = 5, message = "workStatusCd는 최소 1자리 이상 최대 5자리 이하여야 합니다.")
    private String workStatusCd;

    @Size(max = 6, message = "bankName은 최대 6자리 이하입니다.")
    private String bankName;

    @Size(max = 20, message = "bankAccountNo은 최대 20자리 이하입니다.")
    private String bankAccountNo;

    @PositiveOrZero(message = "monthSalary은 0이상입니다.")
    private Integer monthSalary;

    @PositiveOrZero(message = "hourWage은 0이상입니다.")
    private Integer hourWage;

    private String otp;

    @NotBlank(message = "createdBy는 공란일 수 없습니다.")
    private String createdBy;

    private String createdDate;

    @NotBlank(message = "lastUpdatedBy는 공란일 수 없습니다.")
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


