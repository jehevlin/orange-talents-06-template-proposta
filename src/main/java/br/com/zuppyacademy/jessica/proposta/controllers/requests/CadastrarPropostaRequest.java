package br.com.zuppyacademy.jessica.proposta.controllers.requests;

import br.com.zuppyacademy.jessica.proposta.models.Proposta;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CadastrarPropostaRequest {

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

    public Proposta toModel() {
        return new Proposta(nome, email, documento, endereco, salario);
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}
