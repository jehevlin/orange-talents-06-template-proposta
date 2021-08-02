package br.com.zuppyacademy.jessica.proposta.clients.sistemaContas;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "sistemaContas", url = "${external.api.contas}")
public interface SistemaContasClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/contas", consumes = "application/json")
    List<CartaoResponse> ObterCartoes();
}
