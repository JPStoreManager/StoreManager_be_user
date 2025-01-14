package manage.store.DTO.find;

import lombok.*;
import manage.store.exception.InvalidParameterException;
import org.springframework.util.StringUtils;

@Getter @Setter
@ToString
public class FindUserPwSession {

    private final Step[] stepOrder = {Step.NONE, Step.USER_INFO, Step.OTP, Step.NEW_PWD, Step.END};

    private Step completedStep;
    private String userId;
    private String userEmail;

    public FindUserPwSession(Step completedStep, String userId, String userEmail) {
        if(completedStep == null || !StringUtils.hasText(userId) || !StringUtils.hasText(userEmail))
            throw new InvalidParameterException("The parameter is invalid. " + completedStep + ", " + userId + ", " + userEmail);

        this.completedStep = completedStep;
        this.userId = userId;
        this.userEmail = userEmail;
    }

    /**
     * 다음 비밀번호 찾기의 Step을 조회한다.
     * @return NONE - 아무 Step도 진행되지 않았을 경우 <br>
     * END - 모든 비밀번호 찾기 시나리오가 종료되었을 때
     */
    public Step getNextStep() {
        if(completedStep.equals(Step.END)) return Step.END;

        for (int i = 0; i < stepOrder.length; i++) {
            if(stepOrder[i].equals(completedStep)) {
                return stepOrder[i + 1];
            }
        }
        return Step.NONE;
    }

    public enum Step {
        NONE("none"),
        USER_INFO("userInfo"),
        OTP("otp"),
        NEW_PWD("newPassword"),
        END("end");

        private final String step;

        Step(String step) {
            this.step = step;
        }

        public String getStep(){
            return this.step;
        }
    }
}
