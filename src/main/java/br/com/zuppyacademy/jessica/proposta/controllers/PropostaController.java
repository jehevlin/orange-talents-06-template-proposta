package br.com.zuppyacademy.jessica.proposta.controllers;

import br.com.zuppyacademy.jessica.proposta.clients.sistemaFinanceiro.SistemaFinanceiroClient;
import br.com.zuppyacademy.jessica.proposta.clients.sistemaFinanceiro.SolicitacaoRequest;
import br.com.zuppyacademy.jessica.proposta.clients.sistemaFinanceiro.SolicitacaoResponse;
import br.com.zuppyacademy.jessica.proposta.controllers.responses.DetalhesPropostaResponse;
import br.com.zuppyacademy.jessica.proposta.models.EstadoProposta;
import br.com.zuppyacademy.jessica.proposta.models.Proposta;
import br.com.zuppyacademy.jessica.proposta.repositories.PropostaRepository;
import br.com.zuppyacademy.jessica.proposta.controllers.requests.CadastrarPropostaRequest;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path = "propostas")
public class PropostaController {

    private final PropostaRepository propostaRepository;
    private final SistemaFinanceiroClient sistemaFinanceiroClient;

    public PropostaController(
            PropostaRepository repository, SistemaFinanceiroClient sistemaFinanceiroClient) {
        this.propostaRepository = repository;
        this.sistemaFinanceiroClient = sistemaFinanceiroClient;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrarProposta(
            @Valid @RequestBody CadastrarPropostaRequest request,
            UriComponentsBuilder uriBuilder) {

        Optional<Proposta> consultaProposta = propostaRepository.findByDocumento(request.getDocumento());
        if (consultaProposta.isPresent()) {
            return ResponseEntity.unprocessableEntity().build();
        }

        Proposta proposta = propostaRepository.save(request.toModel());

        verificarElegibilidade(proposta);

        URI uri = uriBuilder
                .path("/propostas/{id}")
                .buildAndExpand(proposta.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    private void verificarElegibilidade(Proposta proposta) {
        try {
            SolicitacaoResponse response = sistemaFinanceiroClient.obterSituacao(new SolicitacaoRequest(
                    proposta.getDocumento(),
                    proposta.getNome(),
                    proposta.getId().toString()));

            if (response.getResultadoSolicitacao().equals("SEM_RESTRICAO")) {
                proposta.setEstado(EstadoProposta.ELEGIVEL);
            }
        } catch (FeignException.FeignClientException e) {
            if (e.status() == 422) {
                proposta.setEstado(EstadoProposta.NAO_ELEGIVEL);
            }
        }

        propostaRepository.save(proposta);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> detalheProposta(@PathVariable(name = "id") long idProposta) {
        Optional<Proposta> buscaProposta = propostaRepository.findById(idProposta);
        if (buscaProposta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        DetalhesPropostaResponse response = new DetalhesPropostaResponse(buscaProposta.get());
        return ResponseEntity.ok(response);
    }
}