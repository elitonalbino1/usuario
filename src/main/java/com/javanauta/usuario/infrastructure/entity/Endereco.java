package com.javanauta.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.io.Serial;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rua", length = 150, nullable = false)
    private String rua;

    @Column(name = "numero", nullable = false)
    private Long numero;

    @Column(name = "complemento", length = 50)
    private String complemento;

    @Column(name = "cidade", length = 150, nullable = false)
    private String cidade;

    @Column(name = "estado", length = 2, nullable = false)
    private String estado;

    @Column(name = "cep", length = 9, nullable = false)
    private String cep;

    @Column(name = "usuario_id")
    private Long usuarioId;
}
