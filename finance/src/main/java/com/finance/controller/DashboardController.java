package com.finance.controller;

import com.finance.dto.response.ApiResponse;
import com.finance.dto.response.DashboardSummaryResponse;
import com.finance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getDashboardSummary() {
        DashboardSummaryResponse response = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponse.ok(response, "Dashboard summary fetched successfully"));
    }
    @GetMapping("/insights/category-breakdown")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getCategoryBreakdown() {
        DashboardSummaryResponse response = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(ApiResponse.ok(response, "Category breakdown fetched successfully"));
    }
}