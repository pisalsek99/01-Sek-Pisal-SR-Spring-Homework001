package com.example.honework001.ticketdto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTicket<T> {
    private boolean success;
    private String message;
    private HttpStatus status;
    private T payload;
    private Instant timestamp;

    public static <T> ResponseTicket<T> createSuccessResponse(String message, T payload) {
        return new ResponseTicket<>(true, message, HttpStatus.OK, payload, Instant.now());
    }

    public static <T> ResponseTicket<T> createErrorResponse(String message, HttpStatus status) {
        return new ResponseTicket<>(false, message, status, null, Instant.now());
    }
}