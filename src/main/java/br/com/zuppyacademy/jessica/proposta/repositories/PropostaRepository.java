package br.com.zuppyacademy.jessica.proposta.repositories;

import br.com.zuppyacademy.jessica.proposta.models.EstadoProposta;
import br.com.zuppyacademy.jessica.proposta.models.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    Optional<Proposta> findByEmail(String email);
    List<Proposta> findAllByEstado(EstadoProposta estado);
}
