package br.com.zuppyacademy.jessica.proposta.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@IdClass(BiometriaId.class)
public class Biometria {

    @Id
    @NotBlank
    private String figerprint;

    @Id
    @NotBlank
    private String cartao;

    private LocalDateTime dataCadastro;

    @Deprecated
    public Biometria() {
    }

    public Biometria(String figerprint, String cartao) {
        this.figerprint = figerprint;
        this.cartao = cartao;
        this.dataCadastro = LocalDateTime.now();
    }

    public String getFigerprint() {
        return figerprint;
    }

    public String getCartao() {
        return cartao;
    }
}
