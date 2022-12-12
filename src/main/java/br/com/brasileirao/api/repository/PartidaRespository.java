package br.com.brasileirao.api.repository;

import br.com.brasileirao.api.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaRespository extends JpaRepository<Partida, Long> {
}
