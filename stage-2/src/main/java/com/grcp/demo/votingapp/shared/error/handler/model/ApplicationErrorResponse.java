package com.grcp.demo.votingapp.shared.error.handler.model;

import java.util.List;

public record ApplicationErrorResponse(
        List<DetailedErrorResponse> errors) {
}
