package br.com.zuppyacademy.jessica.proposta.controllers.requests;

import br.com.zuppyacademy.jessica.proposta.models.EstadoProposta;
import br.com.zuppyacademy.jessica.proposta.models.Proposta;

public class DetalhesPropostaResponse {
    private Long id;
    private String nome;
    private EstadoProposta estadoProposta;

    public DetalhesPropostaResponse(Proposta proposta) {
        this.id = proposta.getId();
        this.nome = proposta.getNome();
        this.estadoProposta = proposta.getEstado();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public EstadoProposta getEstadoProposta() {
        return estadoProposta;
    }
}
