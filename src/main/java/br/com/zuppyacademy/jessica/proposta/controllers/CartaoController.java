package br.com.zuppyacademy.jessica.proposta.controllers;

import br.com.zuppyacademy.jessica.proposta.models.BloqueioCartao;
import br.com.zuppyacademy.jessica.proposta.models.Cartao;
import br.com.zuppyacademy.jessica.proposta.repositories.BloqueioCartaoRepository;
import br.com.zuppyacademy.jessica.proposta.repositories.CartaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping(path = "cartoes")
public class CartaoController {

    private final CartaoRepository cartaoRepository;
    private final BloqueioCartaoRepository bloqueioCartaoRepository;

    public CartaoController(CartaoRepository cartaoRepository, BloqueioCartaoRepository bloqueioCartaoRepository) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioCartaoRepository = bloqueioCartaoRepository;
    }

    @PatchMapping(path = "/{id}/bloquear")
    @Transactional
    public ResponseEntity<?> bloquearCartao(
            @PathVariable(name = "id") long idCartao,
            HttpServletRequest request) {
        Optional<Cartao> buscaCartao = cartaoRepository.findById(idCartao);
        if (buscaCartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cartao cartao = buscaCartao.get();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        if (cartao.getBloqueio() != null) {
            return ResponseEntity.unprocessableEntity().build();
        }

        BloqueioCartao bloqueio = new BloqueioCartao(cartao, ipAddress, userAgent);
        cartao.bloquearCartao(bloqueio);

        bloqueioCartaoRepository.save(bloqueio);
        cartaoRepository.save(cartao);

        return ResponseEntity.ok().build();
    }
}
