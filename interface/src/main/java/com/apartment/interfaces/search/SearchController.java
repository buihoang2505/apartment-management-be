package com.apartment.interfaces.search;

import com.apartment.app.search.dto.SearchResponse;
import com.apartment.app.search.handler.SearchQueryHandler;
import com.apartment.interfaces.shared.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Search", description = "Tìm kiếm toàn cục")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchQueryHandler searchQueryHandler;

    @Operation(summary = "Tìm kiếm apartments, employees, portfolios theo keyword (min 2 ký tự)")
    @GetMapping
    public ResponseEntity<CommonResponse<SearchResponse>> search(
            @RequestParam("q") String q) {
        return ResponseEntity.ok(CommonResponse.ok(searchQueryHandler.search(q)));
    }
}
