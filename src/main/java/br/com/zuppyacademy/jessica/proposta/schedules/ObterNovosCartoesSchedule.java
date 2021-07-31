package br.com.zuppyacademy.jessica.proposta.schedules;

import br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.CartaoResponse;
import br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.SistemaContasClient;
import br.com.zuppyacademy.jessica.proposta.models.Proposta;
import br.com.zuppyacademy.jessica.proposta.repositories.PropostaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ObterNovosCartoesSchedule {

    private final PropostaRepository propostaRepository;
    private final SistemaContasClient sistemaContasClient;

    public ObterNovosCartoesSchedule(PropostaRepository propostaRepository, SistemaContasClient sistemaContasClient) {
        this.propostaRepository = propostaRepository;
        this.sistemaContasClient = sistemaContasClient;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    void obterNovosCartoes() {

        List<Proposta> propostasSemCartao = propostaRepository.findAllByNumeroCartao(null);
        if (propostasSemCartao.isEmpty()) return;

        Map<String, String> cartoesEmitidos = obterCartoes();

        List<Proposta> propostas = propostasSemCartao.stream()
                .peek(proposta -> {
                    String idProposta = proposta.getId().toString();
                    if (cartoesEmitidos.containsKey(idProposta)) {
                        proposta.setNumeroCartao(cartoesEmitidos.get(idProposta));
                    }
                })
                .collect(Collectors.toList());

        propostaRepository.saveAll(propostas);
    }

    private Map<String, String> obterCartoes() {
        try {
            return sistemaContasClient.ObterCartoes().stream()
                    .collect(Collectors.toMap(
                            CartaoResponse::getIdProposta,
                            CartaoResponse::getId));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
