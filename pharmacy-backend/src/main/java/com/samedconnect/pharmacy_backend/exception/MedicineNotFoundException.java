package com.samedconnect.pharmacy_backend.exception;

public class MedicineNotFoundException extends RuntimeException {

    public MedicineNotFoundException(Long id) {
        super("Medicine with id " + id + " was not found");
    }
}
