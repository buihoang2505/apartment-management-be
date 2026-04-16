Review toàn bộ code thay đổi hiện tại (hoặc file được chỉ định) theo checklist sau.
Với mỗi vấn đề tìm được, báo cáo: file + số dòng, mô tả vấn đề, code fix cụ thể.
Cuối cùng cho điểm tổng thể: PASS / NEEDS FIX / BLOCKED.

---

## CHECKLIST

### [ARCH] Architecture
- [ ] Dependency direction đúng chưa? (infra → app → domain, không được đảo)
- [ ] Controller có inject Repository trực tiếp không? (phải qua Handler)
- [ ] Entity có bị return thẳng ra ngoài không? (phải map qua DTO)
- [ ] CommandHandler và QueryHandler có bị mix logic không?

### [SPRING] Spring / JPA
- [ ] @PathVariable và @RequestParam có explicit name không?
      VD: `@PathVariable("id")` chứ không phải `@PathVariable`
- [ ] QueryHandler có `@Transactional(readOnly = true)` ở class level không?
- [ ] CommandHandler có `@Transactional` ở method level không?
- [ ] FetchType có dùng EAGER không? (phải giải thích nếu có)
- [ ] Có nguy cơ N+1 query không? (lazy load trong loop)
- [ ] Request DTO có `@Valid` annotation ở Controller không?

### [CODE] Code quality
- [ ] Có hardcode string/số magic number không? (nên dùng constant/enum)
- [ ] Exception message có đủ context không? (vd: "Zone not found: " + id)
- [ ] Có dùng `record` cho DTO immutable chưa?
- [ ] Response DTO có static `from(entity)` factory method không?
- [ ] Lombok annotation đúng loại chưa? (@SuperBuilder cho entity kế thừa BaseEntity)

### [SECURITY]
- [ ] Endpoint mới có được khai báo trong SecurityConfig chưa?
- [ ] Endpoint nào cần auth mà đang để public không?

### [AUDIT]
- [ ] Write operation nào quan trọng mà chưa ghi AuditLog không?
      (create/update/delete của Apartment, Zone, Portfolio, User)

### [NAMING]
- [ ] Package đúng pattern `com.apartment.<module>.<domain>.<layer>` không?
- [ ] Handler tên đúng `<Domain>CommandHandler` / `<Domain>QueryHandler` không?
- [ ] Table name dùng snake_case không?
