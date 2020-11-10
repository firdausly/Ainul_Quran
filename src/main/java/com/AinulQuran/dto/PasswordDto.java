package com.AinulQuran.dto;


import com.AinulQuran.constraint.FieldMatch;

import javax.validation.constraints.NotEmpty;


@FieldMatch(first = "newPass", second = "confirmPass", message = "The password fields must match")
public class PasswordDto {

    @NotEmpty
    private String currentPass;

    @NotEmpty
    private String newPass;

    @NotEmpty
    private String confirmPass;

    public PasswordDto() {
    }


    public String getCurrentPass() {
        return currentPass;
    }


    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

}
