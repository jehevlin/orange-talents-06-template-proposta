package br.com.zuppyacademy.jessica.proposta.models;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @NotBlank
    private String destino;

    @NotNull
    @Future
    private LocalDateTime dataTermino;

    @NotNull
    private LocalDateTime instanteAviso;

    @NotBlank
    private String ipAddress;

    @NotBlank
    private String userAgent;

    @Deprecated
    public AvisoViagem() {
    }

    public AvisoViagem(Cartao cartao, String destino, LocalDateTime dataTermino, String ipAddress, String userAgent) {
        this.cartao = cartao;
        this.destino = destino;
        this.dataTermino = dataTermino;
        this.instanteAviso = LocalDateTime.now();
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDateTime getDataTermino() {
        return dataTermino;
    }

    public LocalDateTime getInstanteAviso() {
        return instanteAviso;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
