package br.com.gs1.gs1.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private String details; // Optinal --> Devlopment Only ((maybe...??))
}