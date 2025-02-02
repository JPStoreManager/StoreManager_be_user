# 테스트 코드 작성법
## 통합 테스트

    // 통합 테스트 의미
    @Tag(Tags.Test.INTEGRATION)

    // 테스트를 위해 넣은 데이터 롤백
    @Transactional

    // Docker test를 위한 임시 테스트 컨테이너
    @Testcontainers

    @SpringBootTest
    @ActiveProfiles(Profiles.TEST)

    // Test 후 Spring Rest Docs 생성을 위한 설정
    @ExtendWith({RestDocumentationExtension.class})
    class 테스트 클랙스 extends BaseIntegration{
        ...
        /** 테스트용 Docker container 생성 */
        @Container
        private static final DockerComposeContainer composeContainer = getDockerComposeContainer();
        static {
            composeContainer.start();
        }
        ...

        // Mock 테스트를 위한 Mock 설정용 context 
        @Autowired
        private WebApplicationContext context;
    
        private MockMvc mockMvc;
        ...

        // Spring rest docs 생성용 Mock과 일반 실패 테스트 Mock 생성을 구분하기 위해 
        // 다르게 Mock 설정
        @BeforeEach
        public void setUp(TestInfo testInfo, RestDocumentationContextProvider restDocumentation) {
            this.mockMvc = getMockMvc(testInfo, context, restDocumentation);   
            ...
        }
        ...
    }


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