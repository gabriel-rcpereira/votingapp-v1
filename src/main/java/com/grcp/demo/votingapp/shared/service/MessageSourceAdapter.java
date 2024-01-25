package com.grcp.demo.votingapp.shared.service;

public interface MessageSourceAdapter {

    String getMessage(String code, Object...args);
}
