package br.com.zuppyacademy.jessica.proposta.controllers.requests;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AvisarViagemRequest {
    @NotBlank
    private String destino;

    @NotNull
    @Future
    private LocalDateTime dataTermino;

    public String getDestino() {
        return destino;
    }

    public LocalDateTime getDataTermino() {
        return dataTermino;
    }
}
