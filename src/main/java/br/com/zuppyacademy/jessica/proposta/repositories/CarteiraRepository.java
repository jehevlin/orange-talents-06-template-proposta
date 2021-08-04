package br.com.zuppyacademy.jessica.proposta.repositories;

import br.com.zuppyacademy.jessica.proposta.models.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarteiraRepository extends JpaRepository<Carteira, Long> {
    List<Carteira> findAllByCartaoId(Long cartaoId);
}
