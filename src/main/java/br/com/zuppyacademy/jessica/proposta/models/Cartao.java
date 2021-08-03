package br.com.zuppyacademy.jessica.proposta.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String numero;

    @NotNull
    @OneToOne
    private Proposta proposta;

    @Deprecated
    public Cartao() {
    }

    public Cartao(String numero, Proposta proposta) {
        this.numero = numero;
        this.proposta = proposta;
    }

    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public Proposta getProposta() {
        return proposta;
    }
}
