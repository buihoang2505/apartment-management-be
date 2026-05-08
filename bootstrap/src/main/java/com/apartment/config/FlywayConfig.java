package com.apartment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@Configuration
public class FlywayConfig {

    /**
     * - Xoá các "ghost row" trong flyway_schema_history khi table do migration đó tạo ra
     *   không còn tồn tại (do rollback / drop manual / merge sai version) — giúp Flyway
     *   chạy lại migration thay vì validate fail.
     * - Gọi repair() để cập nhật checksum nếu file migration đã apply bị chỉnh sửa nhẹ.
     * - Sau đó migrate() bình thường.
     */
    @Bean
    public FlywayMigrationStrategy selfHealingMigrate() {
        return flyway -> {
            try (Connection conn = flyway.getConfiguration().getDataSource().getConnection()) {
                cleanGhostRowIfTableMissing(conn, "16", "bookings");
            } catch (Exception e) {
                log.warn("Flyway self-heal step failed (ignored): {}", e.getMessage());
            }
            flyway.repair();
            flyway.migrate();
        };
    }

    private void cleanGhostRowIfTableMissing(Connection conn, String version, String tableName)
            throws Exception {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(
                    "SELECT to_regclass('public." + tableName + "') AS t");
            rs.next();
            String exists = rs.getString("t");
            rs.close();
            if (exists != null) return;
            int updated = st.executeUpdate(
                    "DELETE FROM flyway_schema_history WHERE version = '" + version + "'");
            if (updated > 0) {
                log.warn("Flyway self-heal: removed ghost row V{} (table '{}' missing) — migration will re-run",
                        version, tableName);
            }
        }
    }
}
