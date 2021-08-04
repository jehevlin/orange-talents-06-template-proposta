package br.com.zuppyacademy.jessica.proposta.clients.sistemaContas;

public class AvisarViagemRequest {
    private String destino;
    private String validoAte;

    public AvisarViagemRequest(String destino, String validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public String getValidoAte() {
        return validoAte;
    }
}
