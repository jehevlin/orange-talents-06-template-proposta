package br.com.zuppyacademy.jessica.proposta.repositories;

import br.com.zuppyacademy.jessica.proposta.models.Biometria;
import br.com.zuppyacademy.jessica.proposta.models.BiometriaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiometriaRepository extends JpaRepository<Biometria, BiometriaId> {
}
