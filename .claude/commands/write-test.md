Viết unit test hoặc integration test cho class/method được chỉ định.

## Quy tắc test trong dự án này

### Test Handler (unit test — mock Repository)
```java
@ExtendWith(MockitoExtension.class)
class PortfolioQueryHandlerTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    @InjectMocks
    private PortfolioQueryHandler handler;

    @Test
    void findById_whenExists_returnsResponse() {
        // Arrange
        var id = UUID.randomUUID();
        var portfolio = Portfolio.builder().id(id).name("Test").build();
        when(portfolioRepository.findById(id)).thenReturn(Optional.of(portfolio));

        // Act
        var result = handler.findById(id);

        // Assert
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Test");
    }

    @Test
    void findById_whenNotExists_throwsNotFoundException() {
        var id = UUID.randomUUID();
        when(portfolioRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> handler.findById(id))
            .isInstanceOf(PortfolioNotFoundException.class)
            .hasMessageContaining(id.toString());
    }
}
```

### Test Controller (slice test — @WebMvcTest)
```java
@WebMvcTest(PortfolioController.class)
class PortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioQueryHandler queryHandler;

    @MockBean
    private PortfolioCommandHandler commandHandler;

    @Test
    void getById_returns200() throws Exception {
        var id = UUID.randomUUID();
        when(queryHandler.findById(id))
            .thenReturn(new PortfolioResponse(id, "Test"));

        mockMvc.perform(get("/api/portfolios/" + id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Test"));
    }
}
```

### Test Repository (integration test — @DataJpaTest)
```java
@DataJpaTest
class ApartmentRepositoryTest {

    @Autowired
    private ApartmentRepository repository;

    @Test
    void countByStatus_returnsCorrectCounts() {
        // seed data và assert
    }
}
```

## Checklist sau khi viết test
- [ ] Test name mô tả rõ scenario: `methodName_condition_expectedResult`
- [ ] Mỗi test chỉ assert một behavior
- [ ] Mock chỉ mock dependencies trực tiếp (không mock quá sâu)
- [ ] Exception test kiểm tra cả message, không chỉ type
- [ ] Không dùng `@SpringBootTest` cho unit test của Handler
