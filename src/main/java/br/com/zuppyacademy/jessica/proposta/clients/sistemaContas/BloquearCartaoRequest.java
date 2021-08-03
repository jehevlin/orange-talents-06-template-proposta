package br.com.zuppyacademy.jessica.proposta.clients.sistemaContas;

public class BloquearCartaoRequest {
    private String sistemaResponsavel;

    public BloquearCartaoRequest(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
