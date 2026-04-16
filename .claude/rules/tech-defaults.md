# Tech Defaults

## Java
- Version: 21+
- Dùng `var` khi type rõ ràng từ vế phải
- Prefer `record` cho immutable DTOs
- Dùng `Stream.toList()` thay vì `Collectors.toList()`

## Lombok
| Annotation | Dùng khi |
|---|---|
| `@Getter @Setter` | Entity |
| `@RequiredArgsConstructor` | Handler, Controller, Service |
| `@SuperBuilder` | Entity kế thừa BaseEntity |
| `@NoArgsConstructor` | Entity (cần cho JPA) |
| `@Builder` | DTO, Command object |

## JPA / Hibernate
- FetchType mặc định: **`LAZY`** — không dùng `EAGER` trừ khi có lý do rõ ràng
- Dùng `@Column(name = "snake_case")` explicit
- UUID primary key:
  ```java
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  ```
- Tránh `CascadeType.ALL` nếu không chắc behavior

## Spring Annotations
- **Luôn dùng explicit name** trên path/request params:
  ```java
  @PathVariable("id") UUID id
  @RequestParam("status") String status
  ```
- `@Transactional(readOnly = true)` trên QueryHandler class level
- `@Transactional` trên CommandHandler method level

## Validation
- Dùng Bean Validation (`@NotNull`, `@NotBlank`, `@Valid`) trên request DTOs
- Validate ở Controller input, không validate trong Handler

## Gradle (Multi-module)
```bash
./gradlew :infrastructure:bootRun      # chạy app
./gradlew :domain:test                 # test domain
./gradlew :app:test                    # test app
./gradlew test                         # tất cả
./gradlew build -x test                # build nhanh
```

## Docker
```bash
docker compose up -d          # start postgres + app
docker compose down           # stop
docker compose logs -f app    # xem logs
```
