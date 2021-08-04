package br.com.zuppyacademy.jessica.proposta.controllers;

import br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.*;
import br.com.zuppyacademy.jessica.proposta.controllers.requests.AddCarteiraRequest;
import br.com.zuppyacademy.jessica.proposta.controllers.requests.AvisarViagemRequest;
import br.com.zuppyacademy.jessica.proposta.models.*;
import br.com.zuppyacademy.jessica.proposta.repositories.AvisoViagemRepository;
import br.com.zuppyacademy.jessica.proposta.repositories.BloqueioCartaoRepository;
import br.com.zuppyacademy.jessica.proposta.repositories.CartaoRepository;
import br.com.zuppyacademy.jessica.proposta.repositories.CarteiraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "cartoes")
public class CartaoController {

    private final CartaoRepository cartaoRepository;
    private final BloqueioCartaoRepository bloqueioCartaoRepository;
    private final CarteiraRepository carteiraRepository;
    private final AvisoViagemRepository avisoViagemRepository;
    private final SistemaContasClient sistemaContasClient;
    private final Logger logger = LoggerFactory.getLogger(CartaoController.class);

    public CartaoController(
            CartaoRepository cartaoRepository,
            BloqueioCartaoRepository bloqueioCartaoRepository,
            CarteiraRepository carteiraRepository,
            AvisoViagemRepository avisoViagemRepository,
            SistemaContasClient sistemaContasClient) {
        this.cartaoRepository = cartaoRepository;
        this.bloqueioCartaoRepository = bloqueioCartaoRepository;
        this.carteiraRepository = carteiraRepository;
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
            HttpServletRequest request) {

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

    @PostMapping(path = "/{id}/carteiras/paypal")
    public ResponseEntity<?> addCarteiraPaypal(
            @PathVariable(name = "id") long idCartao,
            @RequestBody @Valid AddCarteiraRequest addCarteiraRequest,
            UriComponentsBuilder uriBuilder) {

        return addCarteira(idCartao, CarteiraDigital.PAYPAL, addCarteiraRequest, uriBuilder);
    }

    @PostMapping(path = "/{id}/carteiras/samsung-pay")
    public ResponseEntity<?> addCarteiraSamsungPay(
            @PathVariable(name = "id") long idCartao,
            @RequestBody @Valid AddCarteiraRequest addCarteiraRequest,
            UriComponentsBuilder uriBuilder) {

        return addCarteira(idCartao, CarteiraDigital.SAMSUNG_PAY, addCarteiraRequest, uriBuilder);
    }

    private ResponseEntity<?> addCarteira(long idCartao, CarteiraDigital carteiraDigital, AddCarteiraRequest addCarteiraRequest, UriComponentsBuilder uriBuilder) {
        Optional<Cartao> buscaCartao = cartaoRepository.findById(idCartao);
        if (buscaCartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cartao cartao = buscaCartao.get();
        String email = addCarteiraRequest.getEmail();

        Optional<Carteira> novaCarteira = getNovaCarteira(cartao, email, carteiraDigital);

        if (novaCarteira.isEmpty()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Carteira carteira = novaCarteira.get();
        carteiraRepository.save(carteira);

        URI uri = getUri(uriBuilder, carteira);
        return ResponseEntity.created(uri).build();
    }

    private URI getUri(UriComponentsBuilder uriBuilder, Carteira carteira) {
        return uriBuilder
                .path("/{idCartao}/carteiras/{idCarteira}")
                .buildAndExpand(carteira.getCartao().getId(), carteira.getId())
                .toUri();
    }

    private Optional<Carteira> getNovaCarteira(Cartao cartao, String email, CarteiraDigital carteiraDigital) {

        List<Carteira> buscaCarteira = carteiraRepository.findAllByCartaoId(cartao.getId());
        boolean jaExiste = buscaCarteira.stream()
                .anyMatch(carteira -> carteira.getCarteiraDigital().equals(carteiraDigital));

        if (jaExiste) {
            return Optional.empty();
        }

        try {
            VincularCarteiraResponse vincularCarteiraResponse = sistemaContasClient.vincularCarteira(
                    new VincularCarteiraRequest(email, carteiraDigital.toString()), cartao.getNumero());
            if (vincularCarteiraResponse.getResultado().equals("ASSOCIADA")) {
                Carteira carteira = new Carteira(cartao, email, carteiraDigital);
                return Optional.of(carteira);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return Optional.empty();
    }
}
