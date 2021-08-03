package br.com.zuppyacademy.jessica.proposta.schedules;

import br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.CartaoResponse;
import br.com.zuppyacademy.jessica.proposta.clients.sistemaContas.SistemaContasClient;
import br.com.zuppyacademy.jessica.proposta.models.Cartao;
import br.com.zuppyacademy.jessica.proposta.models.EstadoProposta;
import br.com.zuppyacademy.jessica.proposta.models.Proposta;
import br.com.zuppyacademy.jessica.proposta.repositories.CartaoRepository;
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
    private final CartaoRepository cartaoRepository;
    private final SistemaContasClient sistemaContasClient;

    public ObterNovosCartoesSchedule(PropostaRepository propostaRepository, CartaoRepository cartaoRepository, SistemaContasClient sistemaContasClient) {
        this.propostaRepository = propostaRepository;
        this.cartaoRepository = cartaoRepository;
        this.sistemaContasClient = sistemaContasClient;
    }

    @Scheduled(fixedDelay = 60000)
    void obterNovosCartoes() {

        List<Proposta> propostasSemCartao = propostaRepository.findAllByEstado(EstadoProposta.ELEGIVEL);
        if (propostasSemCartao.isEmpty()) return;

        Map<String, String> cartoesEmitidos = obterCartoes();

        propostasSemCartao.forEach(proposta -> {
            String idProposta = proposta.getId().toString();
            if (cartoesEmitidos.containsKey(idProposta)) {
                vincularCartao(cartoesEmitidos.get(idProposta), proposta);
            }
        });
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

    @Transactional
    private void vincularCartao(String numeroCartao, Proposta proposta) {
        Cartao cartao = new Cartao(numeroCartao, proposta);
        proposta.setEstado(EstadoProposta.CARTAO_EMITIDO);

        cartaoRepository.save(cartao);
        propostaRepository.save(proposta);
    }
}
