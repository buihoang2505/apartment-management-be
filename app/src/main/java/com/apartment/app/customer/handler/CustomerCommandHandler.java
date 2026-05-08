package com.apartment.app.customer.handler;

import com.apartment.app.apartment.exception.ApartmentNotFoundException;
import com.apartment.app.customer.command.CreateCustomerCommand;
import com.apartment.app.customer.command.DeleteCustomerCommand;
import com.apartment.app.customer.command.UpdateCustomerCommand;
import com.apartment.app.customer.command.UpdateCustomerStatusCommand;
import com.apartment.app.customer.dto.CustomerResponse;
import com.apartment.app.customer.exception.CustomerNotFoundException;
import com.apartment.app.employee.exception.EmployeeNotFoundException;
import com.apartment.app.shared.port.NotificationPort;
import com.apartment.domain.apartment.ApartmentRepository;
import com.apartment.domain.customer.Customer;
import com.apartment.domain.customer.CustomerRepository;
import com.apartment.domain.customer.CustomerStatus;
import com.apartment.domain.customer.LeadSource;
import com.apartment.domain.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerCommandHandler {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final ApartmentRepository apartmentRepository;
    private final NotificationPort notificationPort;

    @Transactional
    public CustomerResponse handle(CreateCustomerCommand cmd) {
        var builder = Customer.builder()
                .fullName(cmd.fullName())
                .email(cmd.email())
                .phone(cmd.phone())
                .source(cmd.source() != null ? cmd.source() : LeadSource.OTHER)
                .status(cmd.status() != null ? cmd.status() : CustomerStatus.NEW)
                .note(cmd.note());

        if (cmd.assignedToId() != null) {
            var emp = employeeRepository.findById(cmd.assignedToId())
                    .orElseThrow(() -> new EmployeeNotFoundException(cmd.assignedToId()));
            builder.assignedTo(emp);
        }
        if (cmd.interestedApartmentId() != null) {
            var apt = apartmentRepository.findById(cmd.interestedApartmentId())
                    .orElseThrow(() -> new ApartmentNotFoundException(cmd.interestedApartmentId()));
            builder.interestedApartment(apt);
        }

        Customer saved = customerRepository.save(builder.build());
        notificationPort.push(
                "Khách hàng mới",
                "Khách hàng " + saved.getFullName() + " vừa được thêm vào hệ thống",
                "CUSTOMER",
                saved.getId().toString());
        return CustomerResponse.from(saved);
    }

    @Transactional
    public CustomerResponse handle(UpdateCustomerCommand cmd) {
        var customer = customerRepository.findById(cmd.id())
                .orElseThrow(() -> new CustomerNotFoundException(cmd.id()));

        customer.setFullName(cmd.fullName());
        customer.setEmail(cmd.email());
        customer.setPhone(cmd.phone());
        customer.setNote(cmd.note());
        if (cmd.source() != null) customer.setSource(cmd.source());
        if (cmd.status() != null) customer.setStatus(cmd.status());

        if (cmd.assignedToId() != null) {
            var emp = employeeRepository.findById(cmd.assignedToId())
                    .orElseThrow(() -> new EmployeeNotFoundException(cmd.assignedToId()));
            customer.setAssignedTo(emp);
        } else {
            customer.setAssignedTo(null);
        }

        if (cmd.interestedApartmentId() != null) {
            var apt = apartmentRepository.findById(cmd.interestedApartmentId())
                    .orElseThrow(() -> new ApartmentNotFoundException(cmd.interestedApartmentId()));
            customer.setInterestedApartment(apt);
        } else {
            customer.setInterestedApartment(null);
        }

        return CustomerResponse.from(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse handle(UpdateCustomerStatusCommand cmd) {
        var customer = customerRepository.findById(cmd.id())
                .orElseThrow(() -> new CustomerNotFoundException(cmd.id()));
        customer.setStatus(cmd.status());
        return CustomerResponse.from(customerRepository.save(customer));
    }

    @Transactional
    public void handle(DeleteCustomerCommand cmd) {
        if (!customerRepository.existsById(cmd.id())) {
            throw new CustomerNotFoundException(cmd.id());
        }
        customerRepository.deleteById(cmd.id());
    }
}
