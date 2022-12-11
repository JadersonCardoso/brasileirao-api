package br.com.brasileirao.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patida")
public class Partida implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partida_id")
    private Long id;

    @Transient
    private String statusPartida;

    @ManyToOne
    @JoinColumn(name = "equipe_casa_id")
    private Equipe equipeCasa;

    @ManyToOne
    @JoinColumn(name = "equipe_visitante_id")
    private Equipe equipeVisitante;

    @Column(name = "placar_equipe_casa")
    private Integer placarEquipeCasa;

    @Column(name = "placar_equipe_visitante")
    private Integer placarEquipeVisitante;

    @Column(name = "gols_equipe_casa")
    private String golsEquipeCasa;

    @Column(name = "gols_equipe_visitante")
    private String golsEquipeVisitante;

    @Column(name = "plcacar_estendido_equipe_casa")
    private Integer placarEstendidoEquipeCasa;

    @Column(name = "placar_estendido_equipe_visitante")
    private Integer placarEstendidoEquipeVisitante;

    @ApiModelProperty(example = "dd/MM/yy HH:mm")
    @JsonFormat(pattern = "dd/MM/yy HH:mm", timezone = "America/Sao_paulo")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_hora_partida")
    private Date dataHoraPartida;

    @Column(name = "local_partida")
    private String localPartida;


}
