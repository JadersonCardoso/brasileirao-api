package br.com.brasileirao.api.service;

import br.com.brasileirao.api.dto.EquipeDTO;
import br.com.brasileirao.api.dto.EquipeResponseDTO;
import br.com.brasileirao.api.entity.Equipe;
import br.com.brasileirao.api.exception.BadRequestException;
import br.com.brasileirao.api.exception.NotFoundException;
import br.com.brasileirao.api.repository.EquipeReporitory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquiperService {

    @Autowired
    private EquipeReporitory equipeReporitory;

    @Autowired
    private ModelMapper modelMapper;

    public Equipe buscarEquipeId(Long id) {
        return this.equipeReporitory.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Não existe equipe com o ID informado: " + id));
    }

    public Equipe buscarEquipePorNome(String nomeEquipe) {
        return this.equipeReporitory.findByNomeEquipe(nomeEquipe)
                .orElseThrow(() -> new NotFoundException("Nenhuma equipe encontrada com o nome informado: " + nomeEquipe));

    }

    public EquipeResponseDTO listarEquipes() {
        return EquipeResponseDTO.builder().equipes(this.equipeReporitory.findAll()).build();
    }

    public Equipe inserirEquipe(EquipeDTO dto) {
        boolean exists = this.equipeReporitory.existsByNomeEquipe(dto.getNomeEquipe());
        if (exists) {
            throw new BadRequestException("Já existe equipe cadastrada com o nome informado.");
        }
        Equipe equipe = this.modelMapper.map(dto, Equipe.class);
        return this.equipeReporitory.save(equipe);
    }

    public void alterarEquipe(Long id, EquipeDTO dto) {
        boolean exists = this.equipeReporitory.existsById(id);
        if (!exists) {
            throw new BadRequestException("Não foi possível alterar a equipe: ID inexistente.");
        }
        Equipe equipe = this.modelMapper.map(dto, Equipe.class);
        equipe.setId(id);
        this.equipeReporitory.save(equipe);
    }


}
