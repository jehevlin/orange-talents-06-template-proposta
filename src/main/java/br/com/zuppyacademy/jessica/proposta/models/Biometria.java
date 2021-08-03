package br.com.zuppyacademy.jessica.proposta.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String figerprint;

    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    private LocalDateTime dataCadastro;

    @Deprecated
    public Biometria() {
    }

    public Biometria(String figerprint, Cartao cartao) {
        this.figerprint = figerprint;
        this.cartao = cartao;
        this.dataCadastro = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
}
