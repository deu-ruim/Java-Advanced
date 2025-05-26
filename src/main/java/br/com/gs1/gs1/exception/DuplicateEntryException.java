package br.com.gs1.gs1.exception;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}