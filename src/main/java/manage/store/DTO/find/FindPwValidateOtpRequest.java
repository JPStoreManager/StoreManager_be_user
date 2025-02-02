package manage.store.DTO.find;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindPwValidateOtpRequest extends FindPwBaseRequest{

    @NotBlank
    private String otp;

    public FindPwValidateOtpRequest() {
        super();
    }

}