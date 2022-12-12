package br.com.brasileirao.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EquipeDTO implements Serializable {

    @NotBlank
    private String nomeEquipe;

    @NotBlank
    private String urlLogoEquipe;
}
