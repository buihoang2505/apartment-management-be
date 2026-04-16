# Workflow Rules

## Git
- Luôn làm việc trên branch `develop`, không commit thẳng vào `main`
- Commit message: `<type>: <mô tả ngắn>`
  - `feat:` — tính năng mới
  - `fix:` — sửa bug
  - `refactor:` — refactor không thay đổi behavior
  - `chore:` — config, build, tooling
  - `docs:` — chỉ thay đổi documentation

## Quy trình thêm feature mới
1. Tạo entity + Repository trong `domain/<feature>/`
2. Tạo DTOs + custom exceptions trong `app/<feature>/`
3. Tạo `<Feature>CommandHandler` và/hoặc `<Feature>QueryHandler` trong `app/<feature>/handler/`
4. Tạo `<Feature>Controller` trong `interface/web/<feature>/`
5. Nếu là write operation quan trọng → ghi vào `AuditLog`

## Quy trình sửa bug
1. Xác định tầng lỗi (domain / app / infrastructure)
2. Viết / cập nhật test trước khi sửa
3. Fix tại đúng tầng, không leaking logic sang tầng khác

## Build & Run
```bash
# Chạy local
./gradlew :infrastructure:bootRun

# Chạy tests
./gradlew test

# Build toàn bộ
./gradlew build

# Docker
docker compose up -d
```
