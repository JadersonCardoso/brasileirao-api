package br.com.brasileirao.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartidaDTO implements Serializable {

    @NotBlank
    private String nomeEquipeCasa;

    @NotBlank
    private String nomeEquipeVisitante;

    @NotBlank
    private String localPatida;

    @NotNull
    @ApiModelProperty(example = "dd/MM/yyyy hh:mm")
    @JsonFormat(shape = Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
    private Date dataHoraPartida;
}
