package com.apartment.app.employee.handler;

import com.apartment.app.department.exception.DepartmentNotFoundException;
import com.apartment.app.employee.command.CreateEmployeeCommand;
import com.apartment.app.employee.command.DeleteEmployeeCommand;
import com.apartment.app.employee.command.UpdateEmployeeCommand;
import com.apartment.app.employee.dto.EmployeeResponse;
import com.apartment.app.employee.exception.EmployeeNotFoundException;
import com.apartment.domain.department.DepartmentRepository;
import com.apartment.domain.employee.Employee;
import com.apartment.domain.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeCommandHandler {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public EmployeeResponse handle(CreateEmployeeCommand cmd) {
        var builder = Employee.builder()
                .fullName(cmd.fullName())
                .email(cmd.email())
                .phone(cmd.phone())
                .position(cmd.position())
                .status(cmd.status() != null ? cmd.status() : com.apartment.domain.employee.EmployeeStatus.ACTIVE);

        if (cmd.departmentId() != null) {
            var department = departmentRepository.findById(cmd.departmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(cmd.departmentId()));
            builder.department(department);
        }

        return EmployeeResponse.from(employeeRepository.save(builder.build()));
    }

    @Transactional
    public EmployeeResponse handle(UpdateEmployeeCommand cmd) {
        var employee = employeeRepository.findById(cmd.id())
                .orElseThrow(() -> new EmployeeNotFoundException(cmd.id()));

        employee.setFullName(cmd.fullName());
        employee.setEmail(cmd.email());
        employee.setPhone(cmd.phone());
        employee.setPosition(cmd.position());
        if (cmd.status() != null) {
            employee.setStatus(cmd.status());
        }

        if (cmd.departmentId() != null) {
            var department = departmentRepository.findById(cmd.departmentId())
                    .orElseThrow(() -> new DepartmentNotFoundException(cmd.departmentId()));
            employee.setDepartment(department);
        } else {
            employee.setDepartment(null);
        }

        return EmployeeResponse.from(employeeRepository.save(employee));
    }

    @Transactional
    public void handle(DeleteEmployeeCommand cmd) {
        if (!employeeRepository.existsById(cmd.id())) {
            throw new EmployeeNotFoundException(cmd.id());
        }
        employeeRepository.deleteById(cmd.id());
    }
}
