package manage.store.DTO.find;

import lombok.Getter;
import manage.store.DTO.common.BaseResponse;
import manage.store.consts.SuccessFlag;
import org.springframework.util.StringUtils;

@Getter
public class FindPwBaseResponse extends BaseResponse {

    private final String sessionId;

    /**
     * @param isSuccess 단계의 성공 여부
     * @param msg 성공/실패에 대한 메세지
     * @param sessionId 성공 시 발급되는 세션 아이디
     *                  실패 시 null
     * @throws IllegalArgumentException 성공했는데 sessionId가 비어있거나, 실패했는데 sessionId가 있는 경우
     */
    public FindPwBaseResponse(SuccessFlag isSuccess, String msg, String sessionId) {
        super(isSuccess, msg);

        if((isSuccess.isSuccess() && !StringUtils.hasText(sessionId)) ||
                (!isSuccess.isSuccess() && StringUtils.hasText(sessionId)))
            throw new IllegalArgumentException("The parameter is invalid. " + sessionId);

        this.sessionId = sessionId;
    }
}
