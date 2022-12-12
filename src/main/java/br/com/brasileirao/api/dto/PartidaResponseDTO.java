package br.com.brasileirao.api.dto;

import br.com.brasileirao.api.entity.Partida;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartidaResponseDTO implements Serializable {

    private List<Partida> partidas;
}
