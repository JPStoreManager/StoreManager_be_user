package manage.store.DTO.find;

import lombok.Getter;
import manage.store.DTO.common.BaseResponse;
import manage.store.consts.SuccessFlag;
import manage.store.exception.InvalidParameterException;
import org.springframework.util.StringUtils;

@Getter
public class FindPwSendOtpResponse extends BaseResponse {

    private final String sessionId;

    /**
     * success인 경우 모든 파라미터는 필수. fail인 경우 isSuccess와 msg만 필수
     */
    public FindPwSendOtpResponse(SuccessFlag isSuccess, String msg, String sessionId) {
        super(isSuccess, msg);

        if(isSuccess.isSuccess() && !StringUtils.hasText(sessionId))
            throw new InvalidParameterException("The parameter is invalid. " + sessionId);

        this.sessionId = sessionId;
    }

    /**
     * fail인 경우에만 호출
     * @param isSuccess
     * @param msg
     */
    public FindPwSendOtpResponse(SuccessFlag isSuccess, String msg) {
        this(isSuccess, msg, null);
    }

}
