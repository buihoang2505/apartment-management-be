package com.apartment.interfaces.dashboard;

import com.apartment.app.dashboard.dto.DashboardStatsResponse;
import com.apartment.app.dashboard.handler.DashboardQueryHandler;
import com.apartment.interfaces.shared.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Dashboard", description = "Thống kê tổng quan")
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardQueryHandler dashboardQueryHandler;

    @GetMapping("/stats")
    public ResponseEntity<CommonResponse<DashboardStatsResponse>> getStats(
            @RequestParam(value = "zoneId", required = false) UUID zoneId) {
        return ResponseEntity.ok(CommonResponse.ok(dashboardQueryHandler.getStats(zoneId)));
    }
}