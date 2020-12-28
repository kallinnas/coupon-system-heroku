package com.jb.cs.data.model;

import org.springframework.http.HttpStatus;

public class CustomerErrorResponse {
    private HttpStatus status;
    private String massage;
    private long timestamp;

    private CustomerErrorResponse(HttpStatus status, String massage, long timestamp) {
        this.status = status;
        this.massage = massage;
        this.timestamp = timestamp;
    }

    public static CustomerErrorResponse of(HttpStatus status, String massage) {
        return new CustomerErrorResponse(status, massage, System.currentTimeMillis());
    }
}
