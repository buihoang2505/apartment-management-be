package com.apartment.app.department.handler;

import com.apartment.app.department.dto.DepartmentResponse;
import com.apartment.app.department.exception.DepartmentNotFoundException;
import com.apartment.domain.department.DepartmentRepository;
import com.apartment.domain.employee.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;

    public Page<DepartmentResponse> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable)
                .map(dept -> DepartmentResponse.from(dept,
                        employeeRepository.countByDepartment_Id(dept.getId())));
    }

    public DepartmentResponse findById(UUID id) {
        var dept = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
        return DepartmentResponse.from(dept, employeeRepository.countByDepartment_Id(id));
    }
}
