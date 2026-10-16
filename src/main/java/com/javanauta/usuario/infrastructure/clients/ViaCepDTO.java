package com.javanauta.usuario.infrastructure.clients;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViaCepDTO implements Serializable {

        private static final long serialVersionUID = 1L;

        private String cep;
        private String logradouro;
        private String complemento;
        private String unidade;
        private String bairro;
        private String localidade;
        private String uf;
        private String estado;
        private String regiao;
        private String ibge;
        private String gia;
        private String ddd;
        private String siafi;
}



