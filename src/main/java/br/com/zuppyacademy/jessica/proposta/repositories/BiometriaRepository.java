package br.com.zuppyacademy.jessica.proposta.repositories;

import br.com.zuppyacademy.jessica.proposta.models.Biometria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiometriaRepository extends JpaRepository<Biometria, Long> {
}
