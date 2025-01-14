package manage.store.DTO.find;

public class FindPwValidateOtpRequest extends FindPwBaseRequest{

    public FindPwValidateOtpRequest(String userId, String email) {
        super(userId, email);
    }

    public FindPwValidateOtpRequest() {
        super();
    }

}