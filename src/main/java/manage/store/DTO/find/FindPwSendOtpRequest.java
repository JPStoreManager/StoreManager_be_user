package manage.store.DTO.find;

public class FindPwSendOtpRequest extends FindPwBaseRequest{

    public FindPwSendOtpRequest(String userId, String email) {
        super(userId, email);
    }

    public FindPwSendOtpRequest() {
        super();
    }

}
