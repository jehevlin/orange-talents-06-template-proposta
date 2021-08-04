package br.com.zuppyacademy.jessica.proposta.clients.sistemaContas;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "sistemaContas", url = "${external.api.contas}")
public interface SistemaContasClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/contas", consumes = "application/json")
    List<CartaoResponse> obterCartoes();

    @RequestMapping(method = RequestMethod.POST, value = "/api/cartoes/{idCartao}/bloqueios", consumes = "application/json")
    BloquearCartaoResponse bloquearCartao(BloquearCartaoRequest request, @PathVariable("idCartao") String idCartao);

    @RequestMapping(method = RequestMethod.POST, value = "/api/cartoes/{idCartao}/avisos", consumes = "application/json")
    AvisarViagemResponse avisarViagem(AvisarViagemRequest request, @PathVariable("idCartao") String idCartao);
}
