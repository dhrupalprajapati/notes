package com.gotprint.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private int statusCode;
    private String message;
    private String timestamp;
}
