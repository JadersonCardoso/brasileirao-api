package br.com.brasileirao.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipe")
public class Equipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipe_id")
    private Long id;
    @Column(name = "nome_equipe")
    private String nomeEquipe;
    @Column(name = "url_logo_equipe")
    private String urlLogoEquipe;
}
