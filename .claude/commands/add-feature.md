Thêm feature mới vào dự án apartment-management theo đúng CQRS pattern.

Nếu chưa có đủ thông tin, hỏi trước:
1. Tên entity/domain là gì? (vd: Contract, Tenant, Payment)
2. Các fields cần thiết?
3. Cần những operations nào? (list / getById / create / update / delete)
4. Có cần AuditLog không?
5. Endpoint cần auth hay public?

Sau khi có đủ thông tin, tạo các file theo đúng thứ tự sau:

---

## BƯỚC 1 — domain/<feature>/

**Entity:**
```java
package com.apartment.domain.<feature>;

import com.apartment.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "<features>")
@Getter @Setter @NoArgsConstructor @SuperBuilder
public class <Entity> extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // fields với @Column(name = "snake_case") explicit
}
```

**Repository:**
```java
package com.apartment.domain.<feature>;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface <Entity>Repository extends JpaRepository<<Entity>, UUID> {
    // custom queries nếu cần — dùng @Query hoặc method name
}
```

---

## BƯỚC 2 — app/<feature>/dto/

**Request** (dùng record + Bean Validation):
```java
package com.apartment.app.<feature>.dto;

import jakarta.validation.constraints.*;

public record <Entity>Request(
    @NotBlank String name
    // các fields khác
) {}
```

**Response** (dùng record + static factory):
```java
package com.apartment.app.<feature>.dto;

public record <Entity>Response(
    UUID id,
    String name
    // các fields khác
) {
    public static <Entity>Response from(<Entity> entity) {
        return new <Entity>Response(
            entity.getId(),
            entity.getName()
        );
    }
}
```

---

## BƯỚC 3 — app/<feature>/exception/

```java
package com.apartment.app.<feature>.exception;

import java.util.UUID;

public class <Entity>NotFoundException extends RuntimeException {
    public <Entity>NotFoundException(UUID id) {
        super("<Entity> not found: " + id);
    }
}
```

---

## BƯỚC 4 — app/<feature>/handler/

**CommandHandler** (write operations):
```java
package com.apartment.app.<feature>.handler;

import com.apartment.app.<feature>.dto.<Entity>Request;
import com.apartment.app.<feature>.dto.<Entity>Response;
import com.apartment.domain.<feature>.<Entity>;
import com.apartment.domain.<feature>.<Entity>Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class <Entity>CommandHandler {

    private final <Entity>Repository <entity>Repository;

    @Transactional
    public <Entity>Response create(<Entity>Request request) {
        var entity = <Entity>.builder()
            // map từ request
            .build();
        return <Entity>Response.from(<entity>Repository.save(entity));
    }

    @Transactional
    public <Entity>Response update(UUID id, <Entity>Request request) {
        var entity = <entity>Repository.findById(id)
            .orElseThrow(() -> new <Entity>NotFoundException(id));
        // update fields
        return <Entity>Response.from(<entity>Repository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        if (!<entity>Repository.existsById(id))
            throw new <Entity>NotFoundException(id);
        <entity>Repository.deleteById(id);
    }
}
```

**QueryHandler** (read operations):
```java
package com.apartment.app.<feature>.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class <Entity>QueryHandler {

    private final <Entity>Repository <entity>Repository;

    public List<<Entity>Response> findAll() {
        return <entity>Repository.findAll().stream()
            .map(<Entity>Response::from)
            .toList();
    }

    public <Entity>Response findById(UUID id) {
        return <entity>Repository.findById(id)
            .map(<Entity>Response::from)
            .orElseThrow(() -> new <Entity>NotFoundException(id));
    }
}
```

---

## BƯỚC 5 — infrastructure/web/<feature>/

```java
package com.apartment.infrastructure.web.<feature>;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/<features>")
@RequiredArgsConstructor
public class <Entity>Controller {

    private final <Entity>CommandHandler commandHandler;
    private final <Entity>QueryHandler queryHandler;

    @GetMapping
    public ResponseEntity<List<<Entity>Response>> findAll() {
        return ResponseEntity.ok(queryHandler.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<<Entity>Response> findById(
            @PathVariable("id") UUID id) {  // explicit name BẮT BUỘC
        return ResponseEntity.ok(queryHandler.findById(id));
    }

    @PostMapping
    public ResponseEntity<<Entity>Response> create(
            @RequestBody @Valid <Entity>Request request) {
        return ResponseEntity.status(201).body(commandHandler.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<<Entity>Response> update(
            @PathVariable("id") UUID id,    // explicit name BẮT BUỘC
            @RequestBody @Valid <Entity>Request request) {
        return ResponseEntity.ok(commandHandler.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable("id") UUID id) {  // explicit name BẮT BUỘC
        commandHandler.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## BƯỚC 6 — Checklist trước khi báo done

- [ ] Entity extend BaseEntity với @SuperBuilder
- [ ] Repository trong package domain (không phải app)
- [ ] @PathVariable và @RequestParam có explicit name
- [ ] QueryHandler có @Transactional(readOnly = true) ở class level
- [ ] CommandHandler có @Transactional ở method level
- [ ] Response DTO có static from() factory
- [ ] Exception message rõ ràng có context
- [ ] SecurityConfig có cần update không?
- [ ] AuditLog có cần ghi không?
- [ ] Run `./gradlew :domain:compileJava` và `./gradlew :app:compileJava` để kiểm tra compile
