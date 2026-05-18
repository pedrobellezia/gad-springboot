package com.example.gad.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.gad.models.Usuario;
import com.example.gad.repositories.UsuarioRepository;
import com.example.gad.services.exceptions.DataBindingViolationException;
import com.example.gad.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;


    public List<Usuario> findAll() {
        return this.usuarioRepository.findAll();
    }

    public Usuario findById(UUID id) {

        return this.usuarioRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Usuario nao encontrado! Id: " + id
                ));
    }

    @Transactional
    public Usuario create(Usuario obj) {

        obj.setId(null);

        return this.usuarioRepository.save(obj);
    }

    @Transactional
    public Usuario update(Usuario obj) {

        Usuario newObj = findById(obj.getId());

        if (obj.getNome() != null) {
            newObj.setNome(obj.getNome());
        }

        if (obj.getEmail() != null) {
            newObj.setEmail(obj.getEmail());
        }

        if (obj.getSenha() != null && !obj.getSenha().isBlank()) {
            newObj.setSenha(obj.getSenha());
        }

        if (obj.getAvatar() != null) {
            newObj.setAvatar(obj.getAvatar());
        }

        return this.usuarioRepository.save(newObj);
    }

    @Transactional
    public void delete(UUID id) {

        findById(id);

        try {

            this.usuarioRepository.deleteById(id);

        } catch (Exception e) {

            throw new DataBindingViolationException(
                    "Nao e possivel excluir pois ha entidades relacionadas!"
            );
        }
    }
}