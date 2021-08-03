package br.com.zuppyacademy.jessica.proposta.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class BloqueioCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @NotBlank
    private String ipAddress;

    @NotBlank
    private String userAgent;

    private LocalDateTime instanteBloqueio;

    @Deprecated
    public BloqueioCartao() {
    }

    public BloqueioCartao(Cartao cartao, String ipAddress, String userAgent) {
        this.cartao = cartao;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.instanteBloqueio = LocalDateTime.now();
    }
}
