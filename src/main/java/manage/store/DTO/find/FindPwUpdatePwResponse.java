package manage.store.DTO.find;

import manage.store.consts.SuccessFlag;

public class FindPwUpdatePwResponse extends FindPwBaseResponse {

    /**
     * success인 경우 모든 파라미터는 필수.
     */
    public FindPwUpdatePwResponse(SuccessFlag isSuccess, String msg, String sessionId) {
        super(isSuccess, msg, sessionId);
    }

    /**
     * fail인 경우에만 호출
     */
    public FindPwUpdatePwResponse(SuccessFlag isSuccess, String msg) {
        this(isSuccess, msg, null);
    }

}
