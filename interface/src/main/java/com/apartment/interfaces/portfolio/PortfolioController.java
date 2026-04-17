package com.apartment.interfaces.portfolio;

import com.apartment.app.portfolio.command.*;
import com.apartment.app.portfolio.dto.PortfolioResponse;
import com.apartment.app.portfolio.handler.PortfolioCommandHandler;
import com.apartment.app.portfolio.handler.PortfolioQueryHandler;
import com.apartment.interfaces.portfolio.request.CreatePortfolioRequest;
import com.apartment.interfaces.portfolio.request.UpdatePortfolioRequest;
import com.apartment.interfaces.shared.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Portfolios", description = "Quản lý danh mục bất động sản")
@RestController
@RequestMapping("/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioCommandHandler portfolioCommandHandler;
    private final PortfolioQueryHandler portfolioQueryHandler;

    @Operation(summary = "Lấy danh sách portfolios")
    @GetMapping
    public ResponseEntity<CommonResponse<List<PortfolioResponse>>> getAll() {
        return ResponseEntity.ok(CommonResponse.ok(portfolioQueryHandler.findAll()));
    }

    @Operation(summary = "Lấy portfolio theo ID")
    @ApiResponse(responseCode = "404", description = "Không tìm thấy portfolio")
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<PortfolioResponse>> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(CommonResponse.ok(portfolioQueryHandler.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<PortfolioResponse>> create(
            @Valid @RequestBody CreatePortfolioRequest request) {
        CreatePortfolioCommand cmd = new CreatePortfolioCommand(
                request.name(), request.description(), request.zoneIds());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Tạo danh mục thành công", portfolioCommandHandler.handle(cmd)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<PortfolioResponse>> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdatePortfolioRequest request) {
        UpdatePortfolioCommand cmd = new UpdatePortfolioCommand(
                id, request.name(), request.description(), request.zoneIds());
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật danh mục thành công", portfolioCommandHandler.handle(cmd)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable("id") UUID id) {
        portfolioCommandHandler.handle(new DeletePortfolioCommand(id));
        return ResponseEntity.ok(CommonResponse.ok("Xóa danh mục thành công", null));
    }
}