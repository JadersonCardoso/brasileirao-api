package br.com.brasileirao.api.dto;

import br.com.brasileirao.api.entity.Equipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipeResponseDTO implements Serializable {

    private List<Equipe> equipes;
}
