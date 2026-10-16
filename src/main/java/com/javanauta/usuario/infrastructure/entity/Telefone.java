package com.javanauta.usuario.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.io.Serial;

@Entity
@Table(name = "telefone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Telefone implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
