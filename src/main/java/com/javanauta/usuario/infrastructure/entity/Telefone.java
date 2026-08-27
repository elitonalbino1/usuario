package com.javanauta.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "telefone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", nullable = false, length = 10)
    private String numero;

    @Column(name = "ddd", nullable = false, length = 3)
    private String ddd;

    @Column(name = "usuario_id")
    private Long usuarioId;
}