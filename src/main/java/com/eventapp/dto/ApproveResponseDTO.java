package com.eventapp.dto;

public class ApproveResponseDTO {

    private String message;

    public ApproveResponseDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}