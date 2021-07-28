package br.com.zuppyacademy.jessica.proposta.controllers;

import br.com.zuppyacademy.jessica.proposta.models.Proposta;
import br.com.zuppyacademy.jessica.proposta.repositories.PropostaRepository;
import br.com.zuppyacademy.jessica.proposta.requests.CadastrarPropostaRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "propostas")
public class PropostaController {

    private final PropostaRepository repository;

    public PropostaController (PropostaRepository repository) {this.repository = repository;}

    @PostMapping
    public ResponseEntity<?> cadastrarProposta(
            @Valid @RequestBody CadastrarPropostaRequest request,
            UriComponentsBuilder uriBuilder) {

        Proposta proposta = repository.save(request.toModel());

        URI uri = uriBuilder
                .path("/propostas/{id}")
                .buildAndExpand(proposta.getId())
                .toUri();
        
        return ResponseEntity.created(uri).build();
    }
}
