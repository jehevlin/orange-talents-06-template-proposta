package br.com.zuppyacademy.jessica.proposta.clients.sistemaFinanceiro;

public class SolicitacaoResponse {
    private String documento;
    private String nome;
    private String idProposta;
    private String resultadoSolicitacao;

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public String getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }
}
