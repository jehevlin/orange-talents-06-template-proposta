package br.com.zuppyacademy.jessica.proposta.clients.sistemaContas;

public class VincularCarteiraRequest {
    private String email;
    private String carteira;

    public VincularCarteiraRequest(String email, String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira;
    }
}
