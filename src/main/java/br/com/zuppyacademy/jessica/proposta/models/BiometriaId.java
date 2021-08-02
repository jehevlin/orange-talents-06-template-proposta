package br.com.zuppyacademy.jessica.proposta.models;

import java.io.Serializable;
import java.util.Objects;

public class BiometriaId implements Serializable {
    private String figerprint;
    private String cartao;

    @Deprecated
    public BiometriaId() {}

    public BiometriaId(String figerprint, String cartao) {
        this.figerprint = figerprint;
        this.cartao = cartao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BiometriaId that = (BiometriaId) o;
        return Objects.equals(figerprint, that.figerprint) && Objects.equals(cartao, that.cartao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(figerprint, cartao);
    }
}
