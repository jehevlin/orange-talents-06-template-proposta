package br.com.zuppyacademy.jessica.proposta.controllers;

import br.com.zuppyacademy.jessica.proposta.models.Biometria;
import br.com.zuppyacademy.jessica.proposta.models.Proposta;
import br.com.zuppyacademy.jessica.proposta.repositories.BiometriaRepository;
import br.com.zuppyacademy.jessica.proposta.repositories.PropostaRepository;
import br.com.zuppyacademy.jessica.proposta.requests.CadastrarBiometriaRequest;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(path = "biometrias")
public class BiometriaController {

    private final BiometriaRepository biometriaRepository;
    private final PropostaRepository propostaRepository;

    public BiometriaController(BiometriaRepository biometriaRepository, PropostaRepository propostaRepository) {
        this.biometriaRepository = biometriaRepository;
        this.propostaRepository = propostaRepository;
    }

    @PostMapping(path = "/{cartao}")
    public ResponseEntity<?> cadastrarBiometria(
            @PathVariable(name = "cartao") String cartao,
            @RequestBody @Valid CadastrarBiometriaRequest request) {

        Optional<Proposta> buscaProposta = propostaRepository.findByNumeroCartao(cartao);
        if (cartao == null || cartao.equals("") || buscaProposta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!Base64.isBase64(request.getFingerprint())) {
            return ResponseEntity.badRequest().build();
        }

        Biometria biometria = new Biometria(request.getFingerprint(), cartao);
        biometriaRepository.save(biometria);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
