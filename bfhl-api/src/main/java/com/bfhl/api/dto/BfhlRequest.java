package com.bfhl.api.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Request DTO – carries the input array from the client.
 */
public class BfhlRequest {

    @NotNull(message = "data field must not be null")
    private List<String> data;

    public BfhlRequest() {}

    public BfhlRequest(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
