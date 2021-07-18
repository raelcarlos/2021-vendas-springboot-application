package io.github.israelcarlos.domain.entity;

import io.github.israelcarlos.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    @NotEmpty(message = "campo.login.obrigatorio")
    private String login;
    @Column
    @NotEmpty(message = "campo.senha.obrigatorio")
    private String senha;
    private boolean admin;

}
