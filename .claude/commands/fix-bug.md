Phân tích và fix bug được mô tả. Làm theo đúng thứ tự:

## BƯỚC 1 — Tái hiện vấn đề
Trước khi fix, xác định rõ:
- Bug xảy ra ở tầng nào? (domain / app / infrastructure)
- Trigger là gì? (HTTP request, cron job, startup...)
- Error message hoặc behavior sai là gì?

## BƯỚC 2 — Tìm root cause
Đọc code liên quan theo đúng flow: Controller → Handler → Repository → Entity.
Không fix triệu chứng, fix nguyên nhân gốc.

## BƯỚC 3 — Fix
- Fix tại đúng tầng, không leak logic sang tầng khác
- Không thay đổi behavior của code không liên quan đến bug
- Nếu fix cần thêm method vào Repository → viết JPQL/method name đúng
- Nếu fix liên quan đến @PathVariable/@RequestParam → kiểm tra có explicit name chưa

## BƯỚC 4 — Verify
Sau khi fix, kiểm tra:
- [ ] `./gradlew compileJava` pass không có error
- [ ] Fix không vi phạm CQRS (CommandHandler không có readOnly, QueryHandler không write)
- [ ] Fix không tạo ra N+1 query mới (lazy load trong loop)
- [ ] Không có entity bị expose ra ngoài Controller

## Common bugs trong dự án này
| Bug | Nguyên nhân | Fix |
|---|---|---|
| `IllegalArgumentException: Name for argument...` | @PathVariable thiếu explicit name | `@PathVariable("id")` |
| `LazyInitializationException` | Truy cập lazy field ngoài transaction | Thêm `@Transactional` hoặc dùng fetch join |
| `could not extract ResultSet` | JPQL/SQL sai tên column/table | Kiểm tra @Column(name=...) khớp với query |
| `StackOverflowError` trong JSON | Entity circular reference | Dùng DTO, không serialize entity trực tiếp |
