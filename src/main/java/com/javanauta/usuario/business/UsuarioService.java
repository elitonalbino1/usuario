package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.exceptions.conflictException;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder PasswordEncoder;
    private final JwtUtil jwtUtil;


    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(PasswordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        usuarioRepository.save(usuario);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmail(email);
            if (existe) {
                throw new conflictException("email ja cadastrado" + email);
            }

        } catch (conflictException e) {
            throw new conflictException("Email ja cadastrado" + e.getCause());
        }
    }

    public boolean verificaEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    public Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("Email nao encontrado" + email));
    }
    public void deleteUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);


    }
    public UsuarioDTO atualizaDadosdeUsuario(String token, UsuarioDTO dto){

        //aqui buscamos o email atraves do token(tira a obrigatoriedade do email)
       String email =  jwtUtil.extrairEmailToken(token.substring(7));

       //Criptografia de senha
       dto.setEmail(dto.getSenha() != null ? PasswordEncoder.encode(dto.getSenha()) : null);

       //Busca os dados do Usuario no banco de dados.
       Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
               new ResourceNotFoundException("Email nao encontrado"));
      //mesclou os dados que recebemos na requisiçao do DTO com os dados do bancos de dadso
       Usuario usuario = usuarioConverter.updateUsuario(dto, usuarioEntity);

       //salvou os dados do usuario convertido depois pegou os dados e converteu para usuarioDTO
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));

    }
  }

