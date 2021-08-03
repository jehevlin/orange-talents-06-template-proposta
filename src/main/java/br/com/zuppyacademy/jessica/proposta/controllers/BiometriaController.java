package br.com.zuppyacademy.jessica.proposta.controllers;

import br.com.zuppyacademy.jessica.proposta.controllers.requests.CadastrarBiometriaRequest;
import br.com.zuppyacademy.jessica.proposta.models.Biometria;
import br.com.zuppyacademy.jessica.proposta.models.Cartao;
import br.com.zuppyacademy.jessica.proposta.repositories.BiometriaRepository;
import br.com.zuppyacademy.jessica.proposta.repositories.CartaoRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "biometrias")
public class BiometriaController {

    private final BiometriaRepository biometriaRepository;
    private final CartaoRepository cartaoRepository;
    private final Logger logger = LoggerFactory.getLogger(BiometriaController.class);

    public BiometriaController(BiometriaRepository biometriaRepository, CartaoRepository cartaoRepository) {
        this.biometriaRepository = biometriaRepository;
        this.cartaoRepository = cartaoRepository;
    }

    @PostMapping(path = "/{cartao}")
    public ResponseEntity<?> cadastrarBiometria(
            @PathVariable(name = "cartao") Long cartaoId,
            @RequestBody @Valid CadastrarBiometriaRequest request) {

        Optional<Cartao> buscaCartao = cartaoRepository.findById(cartaoId);
        if (buscaCartao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Cartao cartao = buscaCartao.get();
        logger.info("Cartao com id={} foi encontrado", cartao.getId());

        if (!Base64.isBase64(request.getFingerprint())) {
            return ResponseEntity.badRequest().build();
        }

        Biometria biometria = new Biometria(request.getFingerprint(), cartao);
        biometriaRepository.save(biometria);
        logger.info("Biometria com id={} criada com sucesso", biometria.getId());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
