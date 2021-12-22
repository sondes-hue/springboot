package com.vermeg.bookland.dtos;

public class VerificationDto {
    private String email;
    private int verification;

    public VerificationDto(String email, int verification) {
        this.email = email;
        this.verification = verification;
    }

    public VerificationDto(String email) {
        this.email = email;
    }

    public VerificationDto() {}

    public String getEmail() {
        return email;
    }

    public int getVerification() {
        return verification;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVerification(int verification) {
        this.verification = verification;
    }
}
