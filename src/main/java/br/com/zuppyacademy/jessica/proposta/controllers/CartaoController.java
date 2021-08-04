package br.com.zuppyacademy.jessica.proposta.controllers;

import br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.AvisarViagemResponse;
import br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.BloquearCartaoRequest;
import br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.BloquearCartaoResponse;
import br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.SistemaContasClient;
import br.com.zuppyacademy.jessica.proposta.controllers.requests.AvisarViagemRequest;
import br.com.zuppyacademy.jessica.proposta.models.AvisoViagem;
import br.com.zuppyacademy.jessica.proposta.models.BloqueioCartao;
import br.com.zuppyacademy.jessica.proposta.models.Cartao;
import br.com.zuppyacademy.jessica.proposta.repositories.AvisoViagemRepository;
import br.com.zuppyacademy.jessica.proposta.repositories.BloqueioCartaoRepository;
import br.com.zuppyacademy.jessica.proposta.repositories.CartaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping(path = "cartoes")
public class CartaoController {

    private final CartaoRepository cartaoRepository;
    private final BloqueioCartaoRepository bloqueioCartaoRepository;
    private final AvisoViagemRepository avisoViagemRepository;
    private final SistemaContasClient sistemaContasClient;

    public CartaoController(
            CartaoRepository cartaoRepository,
            BloqueioCartaoRepository bloqueioCartaoRepository,
            AvisoViagemRepository avisoViagemRepository,
            SistemaContasClient sistemaContasClient) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioCartaoRepository = bloqueioCartaoRepository;
        this.avisoViagemRepository = avisoViagemRepository;
        this.sistemaContasClient = sistemaContasClient;
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

        try {
            BloquearCartaoResponse bloquearCartaoResponse = sistemaContasClient.bloquearCartao(
                    new BloquearCartaoRequest("proposta"), cartao.getNumero()
            );

            if (bloquearCartaoResponse.getResultado().equals("BLOQUEADO")) {
                BloqueioCartao bloqueio = new BloqueioCartao(cartao, ipAddress, userAgent);
                cartao.bloquearCartao(bloqueio);

                bloqueioCartaoRepository.save(bloqueio);
                cartaoRepository.save(cartao);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/{id}/avisarViagem")
    public ResponseEntity<?> avisarViagem(
            @PathVariable(name = "id") long idCartao,
            @RequestBody @Valid AvisarViagemRequest avisarViagemRequest,
            HttpServletRequest request
    ) {
        Optional<Cartao> buscaCartao = cartaoRepository.findById(idCartao);
        if (buscaCartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cartao cartao = buscaCartao.get();
        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        try {
            AvisarViagemResponse avisarViagemResponse =
                    sistemaContasClient.avisarViagem(new br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.AvisarViagemRequest(
                            avisarViagemRequest.getDestino(),
                            avisarViagemRequest.getDataTermino().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    ), cartao.getNumero());

            if (avisarViagemResponse.getResultado().equals("CRIADO")) {
                AvisoViagem avisoViagem = new AvisoViagem(
                        cartao,
                        avisarViagemRequest.getDestino(),
                        avisarViagemRequest.getDataTermino(),
                        ipAddress,
                        userAgent);

                avisoViagemRepository.save(avisoViagem);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
