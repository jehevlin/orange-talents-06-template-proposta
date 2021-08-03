package br.com.zuppyacademy.jessica.proposta.repositories;

import br.com.zuppyacademy.jessica.proposta.models.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
}
