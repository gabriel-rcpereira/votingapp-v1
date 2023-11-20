package com.grcp.demo.votingapp.shared.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ModuleErrorCode {

    POOL_ERROR("001"),
    VOTE("002");

    private final String moduleCode;

    public String formatError(String errorCode) {
        return String.format("%s.%s", moduleCode, errorCode);
    }
}
