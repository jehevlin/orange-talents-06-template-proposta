package br.com.zuppyacademy.jessica.proposta.requests;

import javax.validation.constraints.NotBlank;

public class CadastrarBiometriaRequest {

    @NotBlank
    private String fingerprint;

    public String getFingerprint() {
        return fingerprint;
    }
}
