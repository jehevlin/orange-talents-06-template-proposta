package br.com.zuppyacademy.jessica.proposta.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CarteiraDigital carteiraDigital;

    @Deprecated
    public Carteira() {
    }

    public Carteira(Cartao cartao, String email, CarteiraDigital carteiraDigital) {
        this.cartao = cartao;
        this.email = email;
        this.carteiraDigital = carteiraDigital;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public String getEmail() {
        return email;
    }

    public CarteiraDigital getCarteiraDigital() {
        return carteiraDigital;
    }
}
