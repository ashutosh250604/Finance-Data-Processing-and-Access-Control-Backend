package com.finance.controller;

import com.finance.dto.request.FinancialRecordRequest;
import com.finance.dto.response.ApiResponse;
import com.finance.dto.response.FinancialRecordResponse;
import com.finance.enums.RecordType;
import com.finance.service.FinancialRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@Tag(name = "Financial Records", description = "CRUD operations for financial records")
@SecurityRequirement(name = "bearerAuth")
public class FinancialRecordController {

        @Autowired
        private FinancialRecordService recordService;

        @GetMapping
        @Operation(summary = "Get all records", description = "Returns paginated list with optional filters")
        public ResponseEntity<ApiResponse<Page<FinancialRecordResponse>>> getAllRecords(
                        @Parameter(description = "Filter by type (INCOME/EXPENSE)") @RequestParam(required = false) RecordType type,

                        @Parameter(description = "Filter by category") @RequestParam(required = false) String category,

                        @Parameter(description = "Filter by start date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

                        @Parameter(description = "Filter by end date (YYYY-MM-DD)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

                        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,

                        @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

                Page<FinancialRecordResponse> records = recordService.getAllRecords(
                                type, category, startDate, endDate, page, size);

                return ResponseEntity.ok(
                                ApiResponse.success("Records retrieved successfully", records));
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get record by ID")
        public ResponseEntity<ApiResponse<FinancialRecordResponse>> getRecordById(
                        @PathVariable Long id) {

                FinancialRecordResponse record = recordService.getRecordById(id);
                return ResponseEntity.ok(
                                ApiResponse.success("Record retrieved successfully", record));
        }

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Create new record", description = "Admin only")
        public ResponseEntity<ApiResponse<FinancialRecordResponse>> createRecord(
                        @Valid @RequestBody FinancialRecordRequest request) {

                FinancialRecordResponse record = recordService.createRecord(request);
                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ApiResponse.success("Record created successfully", record));
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Update record", description = "Admin only")
        public ResponseEntity<ApiResponse<FinancialRecordResponse>> updateRecord(
                        @PathVariable Long id,
                        @Valid @RequestBody FinancialRecordRequest request) {

                FinancialRecordResponse record = recordService.updateRecord(id, request);
                return ResponseEntity.ok(
                                ApiResponse.success("Record updated successfully", record));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        @Operation(summary = "Delete record", description = "Admin only")
        public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable Long id) {
                recordService.deleteRecord(id);
                return ResponseEntity.ok(ApiResponse.success("Record deleted successfully"));
        }

        @GetMapping("/categories")
        @Operation(summary = "Get all categories", description = "Returns list of distinct categories")
        public ResponseEntity<ApiResponse<List<String>>> getAllCategories() {
                List<String> categories = recordService.getAllCategories();
                return ResponseEntity.ok(
                                ApiResponse.success("Categories retrieved successfully", categories));
        }
}
