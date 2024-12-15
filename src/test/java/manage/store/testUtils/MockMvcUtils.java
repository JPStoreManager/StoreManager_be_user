package manage.store.testUtils;

import manage.store.config.WebConfiguration;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


public class MockMvcUtils {

    public static MockMvcBuilder configureDefaultMockMvc(WebApplicationContext context) {
        return MockMvcBuilders.webAppContextSetup(context)
                .defaultRequest(get("/").contextPath(WebConfiguration.getContextPath()))
                .defaultRequest(post("/").contextPath(WebConfiguration.getContextPath()))
                .defaultRequest(put("/").contextPath(WebConfiguration.getContextPath()))
                .defaultRequest(patch("/").contextPath(WebConfiguration.getContextPath()))
                .defaultRequest(delete("/").contextPath(WebConfiguration.getContextPath()))
                .defaultRequest(options("/").contextPath(WebConfiguration.getContextPath()));
    }

}
