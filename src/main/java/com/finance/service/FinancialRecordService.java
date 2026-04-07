package com.finance.service;

import com.finance.dto.request.FinancialRecordRequest;
import com.finance.dto.response.FinancialRecordResponse;
import com.finance.entity.FinancialRecord;
import com.finance.entity.User;
import com.finance.enums.RecordType;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.FinancialRecordRepository;
import com.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Financial Record Service - CRUD operations for financial records
 * 
 * Implements:
 * - Create, Read, Update, Delete operations
 * - Filtering by type, category, date range
 * - Pagination support
 */
@Service
public class FinancialRecordService {

    @Autowired
    private FinancialRecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get current authenticated user
     */
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    /**
     * Get all records with optional filters and pagination
     */
    public Page<FinancialRecordResponse> getAllRecords(
            RecordType type,
            String category,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        return recordRepository.findWithFilters(type, category, startDate, endDate, pageable)
                .map(FinancialRecordResponse::fromEntity);
    }

    /**
     * Get record by ID
     */
    public FinancialRecordResponse getRecordById(Long id) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FinancialRecord", "id", id));
        return FinancialRecordResponse.fromEntity(record);
    }

    /**
     * Create new financial record
     */
    public FinancialRecordResponse createRecord(FinancialRecordRequest request) {
        User currentUser = getCurrentUser();

        FinancialRecord record = new FinancialRecord();
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDescription(request.getDescription());
        record.setDate(request.getDate());
        record.setCreatedBy(currentUser);

        record = recordRepository.save(record);
        return FinancialRecordResponse.fromEntity(record);
    }

    /**
     * Update existing record
     */
    public FinancialRecordResponse updateRecord(Long id, FinancialRecordRequest request) {
        FinancialRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FinancialRecord", "id", id));

        // Update fields
        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDescription(request.getDescription());
        record.setDate(request.getDate());

        record = recordRepository.save(record);
        return FinancialRecordResponse.fromEntity(record);
    }

    /**
     * Delete record
     */
    public void deleteRecord(Long id) {
        if (!recordRepository.existsById(id)) {
            throw new ResourceNotFoundException("FinancialRecord", "id", id);
        }
        recordRepository.deleteById(id);
    }

    /**
     * Get records by type
     */
    public List<FinancialRecordResponse> getRecordsByType(RecordType type) {
        return recordRepository.findByType(type)
                .stream()
                .map(FinancialRecordResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get records by category
     */
    public List<FinancialRecordResponse> getRecordsByCategory(String category) {
        return recordRepository.findByCategory(category)
                .stream()
                .map(FinancialRecordResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get records in date range
     */
    public List<FinancialRecordResponse> getRecordsByDateRange(
            LocalDate startDate, LocalDate endDate) {
        return recordRepository.findByDateBetween(startDate, endDate)
                .stream()
                .map(FinancialRecordResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get recent records (top 10)
     */
    public List<FinancialRecordResponse> getRecentRecords() {
        return recordRepository.findTop10ByOrderByDateDescCreatedAtDesc()
                .stream()
                .map(FinancialRecordResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all distinct categories
     */
    public List<String> getAllCategories() {
        return recordRepository.findDistinctCategories();
    }
}
