package com.apartment.interfaces.dashboard;

import com.apartment.app.dashboard.dto.DashboardStatsResponse;
import com.apartment.app.dashboard.handler.DashboardQueryHandler;
import com.apartment.interfaces.shared.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardQueryHandler dashboardQueryHandler;

    @GetMapping("/stats")
    public ResponseEntity<CommonResponse<DashboardStatsResponse>> getStats() {
        return ResponseEntity.ok(CommonResponse.ok(dashboardQueryHandler.getStats()));
    }
}