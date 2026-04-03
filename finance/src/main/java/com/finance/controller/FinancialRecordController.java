package com.finance.controller;

import com.finance.dto.request.CreateRecordRequest;
import com.finance.dto.request.UpdateRecordRequest;
import com.finance.dto.response.ApiResponse;
import com.finance.dto.response.RecordResponse;
import com.finance.model.RecordType;
import com.finance.service.FinancialRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService financialRecordService;

    @PostMapping
    public ResponseEntity<ApiResponse<RecordResponse>> createRecord(
            @Valid @RequestBody CreateRecordRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {

        RecordResponse response = financialRecordService.createRecord(request, currentUser.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "Record created successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RecordResponse>>> getAllRecords(
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<RecordResponse> records =
                financialRecordService.getAllRecords(type, category, startDate, endDate);

        return ResponseEntity.ok(ApiResponse.ok(records, "Records fetched successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecordResponse>> getRecordById(@PathVariable Long id) {
        RecordResponse response = financialRecordService.getRecordById(id);
        return ResponseEntity.ok(ApiResponse.ok(response, "Record fetched successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RecordResponse>> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRecordRequest request) {

        RecordResponse response = financialRecordService.updateRecord(id, request);
        return ResponseEntity.ok(ApiResponse.ok(response, "Record updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable Long id) {
        financialRecordService.deleteRecord(id);
        return ResponseEntity.ok(ApiResponse.ok(null, "Record deleted successfully"));
    }
}