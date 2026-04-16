# Design Patterns

## CQRS
- **Command** = thao tác thay đổi state → có `@Transactional`
- **Query** = đọc dữ liệu → `@Transactional(readOnly = true)`
- Không mix command + query trong cùng một handler
- Controller nhận request → gọi Handler → trả response

## Response Mapping
- Không bao giờ expose entity ra ngoài Controller layer
- Dùng static factory method:
  ```java
  public static PortfolioResponse from(Portfolio portfolio) { ... }
  ```
- Prefer `record` cho DTO immutable (read-only responses)

## Exception Handling
- Mỗi domain có exception riêng kế thừa `RuntimeException`
  ```java
  public class PortfolioNotFoundException extends RuntimeException {
      public PortfolioNotFoundException(UUID id) {
          super("Portfolio not found: " + id);
      }
  }
  ```
- Global exception handler ở infrastructure layer

## Audit Logging
- Mọi write operation quan trọng phải ghi vào `AuditLog`
- Ghi: `action`, `entityType`, `entityId`, `oldValue` (JSON), `newValue` (JSON), `ipAddress`
- Lấy `ipAddress` từ `HttpServletRequest`

## Security
- JWT stateless — không lưu session
- `JwtAuthFilter` chạy trước mọi request (extends `OncePerRequestFilter`)
- `UserDetailsServiceImpl` load user từ DB bằng username/email

## Dependency Direction
```
infrastructure → app → domain
                       ↑
               Không được đảo ngược
```
