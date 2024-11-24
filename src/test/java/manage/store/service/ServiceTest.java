package manage.store.service;

import jakarta.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Service 계층 테스트에 공통적으로 사용되는 기능들 정의 클래스
 */
public class ServiceTest {

    protected static Validator validator;

    /**
     *
     */
    protected static void initValidator() {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean;
    }

}
