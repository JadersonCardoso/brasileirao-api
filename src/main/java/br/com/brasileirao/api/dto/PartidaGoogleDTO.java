package br.com.brasileirao.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartidaGoogleDTO implements Serializable {

    private String statusPartida;
    private String tempoPartida;
    //informacoes da equipe da casa
    private String nomeEquipeCasa;
    private String urlLogoEquipeCasa;
    private Integer placarEquipeCasa;
    private String golsEquipeCasa;
    private String placarEstendidoEquipeCasa;
    //informacoes da equipe visitante
    private String nomeEquipeVisitante;
    private String urlLogoEquipeVisitante;
    private Integer placarEquipeVisitante;
    private String golsEquipeVisitante;
    private String placarEstendidoEquipeVisitante;

}
