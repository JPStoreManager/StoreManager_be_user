package manage.store.integration;

import manage.store.testUtils.MockMvcUtils;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

abstract public class BaseIntegration {

    protected static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        protected FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints")
                    .value(StringUtils.collectionToDelimitedString(this.constraintDescriptions.descriptionsForProperty(path), ". ")));
        }
    }

    // TODO 추후 개발 서버 생성 시 URL 삽입
    protected MockMvc setMockMvc(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        return ((DefaultMockMvcBuilder)(MockMvcUtils.configureDefaultMockMvc(context)))
                .apply(documentationConfiguration(restDocumentation)
                        /** URI 설정 */
                        .uris()
                        .withScheme("https")
                        .withHost("devUrl")
                        .withPort(80)
                        .and()
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint())
                )
                .build();
    }

    protected abstract void addDocs(ResultActions result) throws Exception;

}
