Thêm Swagger / OpenAPI documentation vào dự án.

## Bước 1 — Thêm dependency vào infrastructure/build.gradle
```groovy
dependencies {
    // ... dependencies hiện có ...
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0'
}
```

## Bước 2 — Cấu hình OpenAPI bean trong infrastructure
Tạo file `infrastructure/src/main/java/com/apartment/infrastructure/config/OpenApiConfig.java`:
```java
package com.apartment.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Apartment Management API")
                .description("REST API quản lý chung cư")
                .version("1.0.0"))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
    }
}
```

## Bước 3 — Cho phép Swagger UI qua SecurityConfig
Thêm vào phần `permitAll()` trong SecurityConfig:
```java
.requestMatchers(
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/v3/api-docs/**",
    "/v3/api-docs.yaml"
).permitAll()
```

## Bước 4 — Annotate Controllers (tùy chọn nhưng nên có)
```java
@Tag(name = "Portfolios", description = "Quản lý danh mục bất động sản")
@RestController
public class PortfolioController {

    @Operation(summary = "Lấy danh sách portfolios")
    @GetMapping
    public ResponseEntity<List<PortfolioResponse>> findAll() { ... }

    @Operation(summary = "Lấy portfolio theo ID")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy")
    @GetMapping("/{id}")
    public ResponseEntity<PortfolioResponse> findById(
            @PathVariable("id") UUID id) { ... }
}
```

## Truy cập sau khi chạy
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs
