package br.com.zuppyacademy.jessica.proposta.clients.sistemaFinanceiro;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "sistemaFinanceiro", url = "${external.api.analise}")
public interface SistemaFinanceiroClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/solicitacao", consumes = "application/json")
    SolicitacaoResponse obterSituacao(SolicitacaoRequest request);
}