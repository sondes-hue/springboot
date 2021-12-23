package com.vermeg.bookland.dtos;

public class ChangePasswordDto {
    private String email, password, repeatPassword;

    public ChangePasswordDto(String email, String password, String repeatPassword) {
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }
    public ChangePasswordDto(String email) {
        this.email = email;
    }
    public ChangePasswordDto() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
