package manage.store.DTO.common;

import manage.store.consts.SuccessFlag;
import manage.store.exception.InvalidParameterException;
import org.springframework.util.StringUtils;

public class ParameterValidationFailResponse extends BaseResponse {

    /**
     * @param msg 오류가 발생한 이유에 대한 메세지
     */
    public ParameterValidationFailResponse(String msg) {
        super(SuccessFlag.N, msg);

        if(!StringUtils.hasText(msg)) {
            throw new InvalidParameterException("The parameter is invalid. " + msg);
        }
    }

}
