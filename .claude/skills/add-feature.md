# Skill: Thêm Feature Mới (CQRS)

Dùng skill này khi được yêu cầu thêm một domain/feature mới vào hệ thống.

## Input cần có
- Tên entity (vd: `Contract`)
- Các fields của entity
- Các operations cần thiết (CRUD / partial)

## Các bước thực hiện

### 1. Domain Layer — `domain/<feature>/`
```java
// Entity
@Entity @Table(name = "<features>")
@Getter @Setter @NoArgsConstructor @SuperBuilder
public class Contract extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // fields...
}

// Repository
public interface ContractRepository extends JpaRepository<Contract, UUID> {
    // custom queries nếu cần
}
```

### 2. App Layer — `app/<feature>/`
```
app/<feature>/
├── dto/
│   ├── ContractRequest.java      ← record hoặc class với @Valid
│   └── ContractResponse.java     ← record với static from()
├── exception/
│   └── ContractNotFoundException.java
└── handler/
    ├── ContractCommandHandler.java
    └── ContractQueryHandler.java
```

### 3. Infrastructure Layer — `infrastructure/web/<feature>/`
```java
@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {
    private final ContractCommandHandler commandHandler;
    private final ContractQueryHandler queryHandler;
    // endpoints...
}
```

### 4. Checklist trước khi done
- [ ] Entity extend BaseEntity đúng không?
- [ ] Repository có trong đúng package domain không?
- [ ] Response có static `from()` factory không?
- [ ] Exception message rõ ràng không?
- [ ] Controller dùng explicit `@PathVariable("id")` không?
- [ ] Write operation có `@Transactional` không?
- [ ] Read operation có `@Transactional(readOnly = true)` không?
- [ ] SecurityConfig có cần update không?
- [ ] Cần ghi AuditLog không?
