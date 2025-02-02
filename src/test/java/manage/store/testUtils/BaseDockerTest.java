package manage.store.testUtils;

import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

public class BaseDockerTest {

    /**
     * Test용 Docker Compose Container 생성 및 반환
     */
    protected static DockerComposeContainer getDockerComposeContainer() {
        return new DockerComposeContainer(new File("./docker-compose.test.yml"));
    }
}
