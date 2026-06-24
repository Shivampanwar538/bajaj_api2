package com.bfhl.api.service;

import com.bfhl.api.dto.BfhlRequest;
import com.bfhl.api.dto.BfhlResponse;

/**
 * Contract for the BFHL processing logic.
 * Having an interface here allows swapping implementations easily
 * and makes unit testing with mocks straightforward.
 */
public interface BfhlService {

    BfhlResponse processData(BfhlRequest request);
}
