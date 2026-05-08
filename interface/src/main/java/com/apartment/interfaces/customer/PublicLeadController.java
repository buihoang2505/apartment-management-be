package com.apartment.interfaces.customer;

import com.apartment.app.customer.command.CreateCustomerCommand;
import com.apartment.app.customer.dto.CustomerResponse;
import com.apartment.app.customer.handler.CustomerCommandHandler;
import com.apartment.domain.customer.CustomerStatus;
import com.apartment.domain.customer.LeadSource;
import com.apartment.interfaces.customer.request.PublicLeadRequest;
import com.apartment.interfaces.shared.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/leads")
@RequiredArgsConstructor
public class PublicLeadController {

    private final CustomerCommandHandler commandHandler;

    @PostMapping
    public ResponseEntity<CommonResponse<CustomerResponse>> register(@Valid @RequestBody PublicLeadRequest req) {
        var cmd = new CreateCustomerCommand(
                req.fullName(),
                req.email(),
                req.phone(),
                req.source() != null ? req.source() : LeadSource.WEBSITE,
                CustomerStatus.NEW,
                null,
                req.interestedApartmentId(),
                req.note()
        );
        CustomerResponse created = commandHandler.handle(cmd);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Đã ghi nhận đăng ký quan tâm", created));
    }
}
