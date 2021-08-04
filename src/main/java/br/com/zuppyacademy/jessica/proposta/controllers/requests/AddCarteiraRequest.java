package br.com.zuppyacademy.jessica.proposta.controllers.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AddCarteiraRequest {

    @NotBlank
    @Email
    private String email;

    public String getEmail() {
        return email;
    }
}
