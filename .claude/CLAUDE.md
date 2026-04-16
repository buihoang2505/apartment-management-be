# Apartment Management — Backend

## Tổng quan
REST API quản lý chung cư/bất động sản. Java / Spring Boot.
Repo BE: https://github.com/buihoang2505/apartment-management-be
Repo FE: https://github.com/buihoang2505/apartment-management-fe

## Tech Stack
- Java 21+ · Spring Boot 3.x · Gradle multi-module
- PostgreSQL (port 5433, db: apartment_db)
- JWT stateless auth · Spring Security
- Spring Data JPA + Hibernate · Lombok · Bean Validation
- Swagger / SpringDoc OpenAPI

## Module Structure
```
apartment-management-be/
├── domain/      ← Entities, Repository interfaces
├── app/         ← CQRS Handlers, DTOs, Exceptions
└── interface/   ← Controllers, SecurityConfig, JWT    ← tên đúng là interface
```

## Gradle Commands
```bash
./gradlew :interface:bootRun        # chạy app
./gradlew :interface:compileJava    # compile check
./gradlew test                      # tất cả tests
./gradlew build -x test             # build nhanh
```

## Architecture: CQRS — KHÔNG được phá vỡ
- `infrastructure → app → domain` — không đảo chiều
- Controller → Handler → Repository (không shortcut)
- Entity KHÔNG ra ngoài Controller — phải qua `static from()` DTO
- CommandHandler = write (`@Transactional` ở method level)
- QueryHandler = read (`@Transactional(readOnly = true)` ở class level)

## Domain Entities
| Entity | Ghi chú |
|---|---|
| `User` | Người dùng, auth |
| `Portfolio` | Danh mục bất động sản |
| `Zone` | Khu vực / tòa nhà |
| `Apartment` | Căn hộ (status, type, area, price, images) |
| `AuditLog` | Lịch sử thao tác |

## Conventions
- Package: `com.apartment.<module>.<domain>.<layer>`
- Primary key: `UUID`
- BaseEntity: `@SuperBuilder` (có createdAt, updatedAt)
- Exception: `<Entity>NotFoundException`
- Response: `static XxxResponse.from(entity)`

## Spring — BẮT BUỘC
```java
@PathVariable("id") UUID id       // explicit name, LUÔN LUÔN
@RequestParam("status") String s  // không được bỏ tên
```

## Known Issues đã fix
- @PathVariable/@RequestParam thiếu explicit name → đã fix toàn bộ controllers
- Module tên là `interface` không phải `infrastructure`
- AuditAspect tạo duplicate log cho ZoneCommandHandler — thu hẹp pointcut sau

## Branching
- `main` — production
- `develop` — working branch (mặc định)
