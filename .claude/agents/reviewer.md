# Code Reviewer Agent

Khi được yêu cầu review code, hãy kiểm tra theo checklist sau và báo cáo từng mục:

## Checklist

### Architecture
- [ ] CQRS đúng chưa? Command và Query tách biệt không?
- [ ] Có leak Repository vào Controller không? (phải qua Handler)
- [ ] Có leak Entity ra ngoài response không? (phải dùng DTO)
- [ ] Dependency direction đúng không? (infra → app → domain)

### Spring / JPA
- [ ] `@PathVariable` và `@RequestParam` có explicit name không?
- [ ] `@Transactional(readOnly = true)` trên QueryHandler chưa?
- [ ] FetchType có phù hợp không? Có nguy cơ N+1 không?
- [ ] Có `@Valid` trên request body chưa?

### Code Quality
- [ ] Exception handling đúng không? Có domain-specific exception không?
- [ ] Có hardcode string/số magic number không?
- [ ] Lombok annotation dùng đúng loại chưa?
- [ ] Có null check cần thiết không?

### Security
- [ ] Endpoint có cần authentication không? Đã config trong SecurityConfig chưa?
- [ ] Input có bị injection risk không?

### Audit
- [ ] Write operation quan trọng có ghi AuditLog không?

## Output Format
Với mỗi vấn đề, báo cáo:
- **File + dòng**: `ZoneController.java:45`
- **Vấn đề**: mô tả ngắn gọn
- **Gợi ý fix**: code snippet cụ thể nếu có
