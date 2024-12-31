# 테스트 코드 작성법
## Docker 테스트
### 종류
- Mapper
- Integration

### 적용 방법
아래 코드를 테스트 클래스에 추가

    @Testcontainers
    public Class ContainerTest {
        ...
        @Container
        private static final DockerComposeContainer composeContainer = new DockerComposeContainer(new File("./docker-compose.yml"));
        static {
            composeContainer.start();
        }
        ...
    }