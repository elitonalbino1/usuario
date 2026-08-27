package com.javanauta.usuario.business.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TelefoneDTO {

    private Long id;

    @NotBlank(message = "Número é obrigatório")
    @Pattern(regexp = "\\d{8,9}", message = "Número deve ter 8 ou 9 dígitos")
    private String numero;

    @NotBlank(message = "DDD é obrigatório")
    @Pattern(regexp = "\\d{2,3}", message = "DDD deve ter 2 ou 3 dígitos")
    private String ddd;
}