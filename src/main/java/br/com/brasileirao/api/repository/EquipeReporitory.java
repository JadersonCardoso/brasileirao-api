package br.com.brasileirao.api.repository;

import br.com.brasileirao.api.entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeReporitory extends JpaRepository<Equipe, Long> {
}
