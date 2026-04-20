package com.apartment.app.department.handler;

import com.apartment.app.department.command.CreateDepartmentCommand;
import com.apartment.app.department.command.DeleteDepartmentCommand;
import com.apartment.app.department.command.UpdateDepartmentCommand;
import com.apartment.app.department.dto.DepartmentResponse;
import com.apartment.app.department.exception.DepartmentNotFoundException;
import com.apartment.app.department.exception.DuplicateDepartmentCodeException;
import com.apartment.domain.department.Department;
import com.apartment.domain.department.DepartmentRepository;
import com.apartment.domain.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentCommandHandler {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public DepartmentResponse handle(CreateDepartmentCommand cmd) {
        if (departmentRepository.existsByCode(cmd.code())) {
            throw new DuplicateDepartmentCodeException(cmd.code());
        }
        var department = Department.builder()
                .name(cmd.name())
                .code(cmd.code())
                .description(cmd.description())
                .build();
        var saved = departmentRepository.save(department);
        return DepartmentResponse.from(saved, employeeRepository.countByDepartment_Id(saved.getId()));
    }

    @Transactional
    public DepartmentResponse handle(UpdateDepartmentCommand cmd) {
        var department = departmentRepository.findById(cmd.id())
                .orElseThrow(() -> new DepartmentNotFoundException(cmd.id()));
        if (departmentRepository.existsByCodeAndIdNot(cmd.code(), cmd.id())) {
            throw new DuplicateDepartmentCodeException(cmd.code());
        }
        department.setName(cmd.name());
        department.setCode(cmd.code());
        department.setDescription(cmd.description());
        var saved = departmentRepository.save(department);
        return DepartmentResponse.from(saved, employeeRepository.countByDepartment_Id(saved.getId()));
    }

    @Transactional
    public void handle(DeleteDepartmentCommand cmd) {
        if (!departmentRepository.existsById(cmd.id())) {
            throw new DepartmentNotFoundException(cmd.id());
        }
        departmentRepository.deleteById(cmd.id());
    }
}
