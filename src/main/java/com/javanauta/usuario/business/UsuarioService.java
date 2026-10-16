package com.javanauta.usuario.business;

import com.javanauta.usuario.business.converter.UsuarioConverter;
import com.javanauta.usuario.business.dto.EnderecoDTO;
import com.javanauta.usuario.business.dto.TelefoneDTO;
import com.javanauta.usuario.business.dto.UsuarioDTO;
import com.javanauta.usuario.infrastructure.entity.Endereco;
import com.javanauta.usuario.infrastructure.entity.Telefone;
import com.javanauta.usuario.infrastructure.entity.Usuario;
import com.javanauta.usuario.infrastructure.exceptions.ConflictException;
import com.javanauta.usuario.infrastructure.exceptions.ResourceNotFoundException;
import com.javanauta.usuario.infrastructure.repository.EnderecoRepository;
import com.javanauta.usuario.infrastructure.repository.TelefoneRepository;
import com.javanauta.usuario.infrastructure.repository.UsuarioRepository;
import com.javanauta.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    // Constantes para evitar duplicação de mensagens
    private static final String EMAIL_NAO_ENCONTRADO = "Email não encontrado: ";
    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado: ";
    private static final String ENDERECO_NAO_ENCONTRADO = "Endereço não encontrado: ";
    private static final String TELEFONE_NAO_ENCONTRADO = "Telefone não encontrado: ";

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO) {
        log.info("Salvando novo usuário: {}", usuarioDTO.getEmail());
        validarEmailUnico(usuarioDTO.getEmail());

        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);

        Usuario salvo = usuarioRepository.save(usuario);
        log.info("Usuário salvo com sucesso. ID: {}", salvo.getId());
        return usuarioConverter.paraUsuarioDTO(salvo);
    }

    private void validarEmailUnico(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            log.warn("Tentativa de cadastro com email existente: {}", email);
            throw new ConflictException("Email já cadastrado: " + email);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        log.info("Buscando usuário por email: {}", email);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(EMAIL_NAO_ENCONTRADO + email));
        return usuarioConverter.paraUsuarioDTO(usuario);
    }

    public void deletaUsuarioPorEmail(String email) {
        log.info("Deletando usuário: {}", email);
        if (!usuarioRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(USUARIO_NAO_ENCONTRADO + email);
        }
        usuarioRepository.deleteByEmail(email);
        log.info("Usuário deletado com sucesso: {}", email);
    }

    public UsuarioDTO atualizaDadosdeUsuario(String token, UsuarioDTO dto) {
        String email = extrairEmailDoToken(token);
        log.info("Atualizando dados do usuário: {}", email);

        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(EMAIL_NAO_ENCONTRADO + email));

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            dto.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        Usuario usuarioAtualizado = usuarioConverter.updateUsuario(dto, usuarioEntity);
        Usuario salvo = usuarioRepository.save(usuarioAtualizado);
        return usuarioConverter.paraUsuarioDTO(salvo);
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO) {
        log.info("Atualizando endereço ID: {}", idEndereco);
        Endereco entity = enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new ResourceNotFoundException(ENDERECO_NAO_ENCONTRADO + idEndereco));

        Endereco atualizado = usuarioConverter.updateEndereco(enderecoDTO, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(atualizado));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO dto) {
        log.info("Atualizando telefone ID: {}", idTelefone);
        Telefone entity = telefoneRepository.findById(idTelefone)
                .orElseThrow(() -> new ResourceNotFoundException(TELEFONE_NAO_ENCONTRADO + idTelefone));

        Telefone atualizado = usuarioConverter.updateTelefone(dto, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(atualizado));
    }

    public EnderecoDTO cadastraEndereco(String token, EnderecoDTO dto) {
        String email = extrairEmailDoToken(token);
        log.info("Cadastrando endereço para: {}", email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(EMAIL_NAO_ENCONTRADO + email));

        Endereco endereco = usuarioConverter.paraEnderecoEntity(dto, usuario.getId());
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO cadastraTelefone(String token, TelefoneDTO dto) {
        String email = extrairEmailDoToken(token);
        log.info("Cadastrando telefone para: {}", email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(EMAIL_NAO_ENCONTRADO + email));

        Telefone telefone = usuarioConverter.paraTelefoneEntity(dto, usuario.getId());
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }

    // Método auxiliar para validar token
    private String extrairEmailDoToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token inválido");
        }
        return jwtUtil.extrairEmailToken(token.substring(7));
    }
}
