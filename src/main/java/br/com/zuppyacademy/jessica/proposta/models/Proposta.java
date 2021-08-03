package br.com.zuppyacademy.jessica.proposta.models;

import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @CPF
    private String documento;

    @NotBlank
    private String endereco;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal salario;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoProposta estado;

    @Deprecated
    public Proposta() {
    }

    public Proposta(Long id) {
        this.id = id;
    }

    public Proposta(String nome, String email, String documento, String endereco, BigDecimal salario) {
        this.nome = nome;
        this.email = email;
        this.documento = documento;
        this.endereco = endereco;
        this.salario = salario;
        this.estado = EstadoProposta.PENDENTE;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setEstado(EstadoProposta estado) {
        this.estado = estado;
    }

    public EstadoProposta getEstado() {
        return estado;
    }
}