package com.apartment.app.employee.handler;

import com.apartment.app.employee.dto.DepartmentEmployeeCountResponse;
import com.apartment.app.employee.dto.EmployeeResponse;
import com.apartment.app.employee.exception.EmployeeNotFoundException;
import com.apartment.domain.department.DepartmentRepository;
import com.apartment.domain.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeQueryHandler {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public Page<EmployeeResponse> findAll(UUID departmentId, Pageable pageable) {
        if (departmentId != null) {
            return employeeRepository.findByDepartment_Id(departmentId, pageable)
                    .map(EmployeeResponse::from);
        }
        return employeeRepository.findAll(pageable).map(EmployeeResponse::from);
    }

    public EmployeeResponse findById(UUID id) {
        return employeeRepository.findById(id)
                .map(EmployeeResponse::from)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public List<DepartmentEmployeeCountResponse> countByDepartment() {
        return departmentRepository.findAll().stream()
                .map(dept -> new DepartmentEmployeeCountResponse(
                        dept.getId(),
                        dept.getName(),
                        dept.getCode(),
                        employeeRepository.countByDepartment_Id(dept.getId())
                ))
                .toList();
    }
}
