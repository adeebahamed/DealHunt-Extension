package com.dealhunt.controller;

import com.dealhunt.dto.DealSearchResponse;
import com.dealhunt.dto.ProductSearchRequest;
import com.dealhunt.service.DealSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deals")
@CrossOrigin(origins = "*")
public class DealSearchController {

    private final DealSearchService dealSearchService;

    public DealSearchController(
            DealSearchService dealSearchService) {

        this.dealSearchService =
                dealSearchService;
    }

    @PostMapping("/search")
    public ResponseEntity<DealSearchResponse> search(
            @RequestBody ProductSearchRequest request) {

        DealSearchResponse response =
                dealSearchService.search(request);

        return ResponseEntity.ok(response);
    }
}