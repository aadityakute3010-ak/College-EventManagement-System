package com.eventapp.dto;


public class RegistrationResponseDTO {

    private long registrationId;
    private String message;

    public RegistrationResponseDTO(long registrationId, String message) {
        this.registrationId = registrationId;
        this.message = message;
    }

    public long getRegistrationId() {
        return registrationId;
    } 

    public void setRegistrationId(long registrationId) {
        this.registrationId = registrationId;
    } 

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
