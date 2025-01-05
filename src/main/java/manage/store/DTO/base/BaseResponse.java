package manage.store.DTO.base;

import lombok.Getter;
import lombok.ToString;
import manage.store.consts.SuccessFlag;

@Getter
@ToString
public class BaseResponse {
    protected final SuccessFlag result;
    protected final String msg;

    public BaseResponse(SuccessFlag isSuccess, String msg) {
        this.result = isSuccess;
        this.msg = msg;
    }
}
