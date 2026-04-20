package com.apartment.domain.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Page<Employee> findByDepartment_Id(UUID departmentId, Pageable pageable);

    @Query("SELECT e.department.id, COUNT(e) FROM Employee e WHERE e.department IS NOT NULL GROUP BY e.department.id")
    List<Object[]> countGroupByDepartment();

    long countByDepartment_Id(UUID departmentId);
}
