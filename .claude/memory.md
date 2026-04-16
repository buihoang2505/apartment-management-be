# Memory — BE

## Đã làm
- Fix @PathVariable/@RequestParam explicit name toàn bộ controllers
- ZoneCommandHandler: đổi .equals() → .equalsIgnoreCase() cho code check
- AuditAspect: đang tạo duplicate log cho Zone operations (chưa fix)

## Gotchas
- Module tên `interface` không phải `infrastructure`
- Gradle run: `./gradlew :interface:bootRun`
- Spring không resolve param names nếu thiếu `-parameters` flag

## TODO
- Thu hẹp pointcut AuditAspect để bỏ duplicate log
- Thêm Swagger annotation cho các Controller
- Thêm phân trang Pageable cho list endpoints
- Viết unit tests cho Handlers
