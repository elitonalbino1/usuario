package com.javanauta.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rua", nullable = false)
    private String rua;

    @Column(name = "numero")
    private Long numero;

    @Column(name = "complemento", length = 50)
    private String complemento;

    @Column(name = "cidade", nullable = false, length = 150)
    private String cidade;

    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    @Column(name = "cep", nullable = false, length = 9)
    private String cep;

    @Column(name = "usuario_id")
    private Long usuarioId;
}