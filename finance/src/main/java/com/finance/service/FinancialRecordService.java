package com.finance.service;

import com.finance.dto.request.CreateRecordRequest;
import com.finance.dto.request.UpdateRecordRequest;
import com.finance.dto.response.RecordResponse;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.FinancialRecord;
import com.finance.model.RecordType;
import com.finance.model.User;
import com.finance.repository.FinancialRecordRepository;
import com.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialRecordService {

    private final FinancialRecordRepository financialRecordRepository;
    private final UserRepository userRepository;

    public RecordResponse createRecord(CreateRecordRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(user)
                .build();

        return RecordResponse.fromEntity(financialRecordRepository.save(record));
    }

    public List<RecordResponse> getAllRecords(RecordType type, String category,
                                              LocalDate startDate, LocalDate endDate) {
        return financialRecordRepository.findByFilters(type, category, startDate, endDate)
                .stream()
                .map(RecordResponse::fromEntity)
                .toList();
    }

    public RecordResponse getRecordById(Long id) {
        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        return RecordResponse.fromEntity(record);
    }

    public RecordResponse updateRecord(Long id, UpdateRecordRequest request) {
        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));

        if (request.getAmount() != null) {
            record.setAmount(request.getAmount());
        }
        if (request.getType() != null) {
            record.setType(request.getType());
        }
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            record.setCategory(request.getCategory());
        }
        if (request.getDate() != null) {
            record.setDate(request.getDate());
        }
        if (request.getNotes() != null) {
            record.setNotes(request.getNotes());
        }

        return RecordResponse.fromEntity(financialRecordRepository.save(record));
    }

    public void deleteRecord(Long id) {
        FinancialRecord record = financialRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with id: " + id));
        financialRecordRepository.delete(record);
    }
}