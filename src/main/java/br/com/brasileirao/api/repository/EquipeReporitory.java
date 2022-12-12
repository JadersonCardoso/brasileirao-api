package br.com.brasileirao.api.repository;

import br.com.brasileirao.api.entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipeReporitory extends JpaRepository<Equipe, Long> {

    public Optional<Equipe> findByNomeEquipe(String nomeEquipe);

    public boolean existsByNomeEquipe(String nomeEquipe);
}
