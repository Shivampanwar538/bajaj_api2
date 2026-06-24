package com.bfhl.api.controller;

import com.bfhl.api.dto.BfhlRequest;
import com.bfhl.api.dto.BfhlResponse;
import com.bfhl.api.service.BfhlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Exposes the single POST /bfhl endpoint.
 */
@RestController
@RequestMapping("/bfhl")
public class BfhlController {

    private final BfhlService bfhlService;

    // Constructor injection — preferred over @Autowired on a field
    public BfhlController(BfhlService bfhlService) {
        this.bfhlService = bfhlService;
    }

    @PostMapping
    public ResponseEntity<BfhlResponse> handleRequest(@Valid @RequestBody BfhlRequest request) {
        BfhlResponse response = bfhlService.processData(request);
        return ResponseEntity.ok(response);
    }
}
