package com.apartment.app.department.handler;

import com.apartment.app.department.dto.DepartmentResponse;
import com.apartment.app.department.exception.DepartmentNotFoundException;
import com.apartment.domain.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentQueryHandler {

    private final DepartmentRepository departmentRepository;

    public Page<DepartmentResponse> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(DepartmentResponse::from);
    }

    public DepartmentResponse findById(UUID id) {
        return departmentRepository.findById(id)
                .map(DepartmentResponse::from)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }
}
