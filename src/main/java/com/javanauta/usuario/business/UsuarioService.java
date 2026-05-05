package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.exceptions.conflictException;
import com.javanauta.usuario.infrastructure.repository.EnderecoRepository;
import com.javanauta.usuario.infrastructure.repository.TelefoneRepository;
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
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;


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
    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(
                    () -> new ResourceNotFoundException("Email nao encontrado " + email)
                            )
            );
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("Email nao encontrado " + email);
        }
    }
    public void deletaUsuarioPorEmail(String email) {
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
        public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
            Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(() ->
                    new ResourceNotFoundException("Endereco nao encontrado" + idEndereco));
            Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);
            enderecoRepository.save(endereco);

            return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
        }
        public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto) {
           Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(() ->
                   new ResourceNotFoundException("Id nao encontrado" + idTelefone));
           Telefone telefone = usuarioConverter.updateTelefone(dto, entity);
           telefoneRepository.save(telefone);

           return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
        }
        public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto) {
        String email =  jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("Email nao encontrado " + email));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        Endereco enderecoEntity = enderecoRepository.save(endereco);
        return usuarioConverter.paraEnderecoDTO(enderecoEntity);

        }
        public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("Email nao encontrado " + email));
        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        Telefone telefoneEntity = telefoneRepository.save(telefone);
        return usuarioConverter.paraTelefoneDTO(telefoneEntity);
        }

    }



