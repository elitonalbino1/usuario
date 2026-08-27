package com.javanauta.usuario.business.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoDTO {

    private Long id;

    @NotBlank(message = "Rua é obrigatória")
    private String rua;

    @NotNull(message = "Número é obrigatório")
    @Positive(message = "Número deve ser positivo")
    private Long numero;

    private String complemento;

    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
    private String estado;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
    private String cep;
}