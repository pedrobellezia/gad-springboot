package com.example.gad.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.gad.models.Redator;
import com.example.gad.models.Usuario;
import com.example.gad.models.dto.RedatorCreateDTO;
import com.example.gad.models.dto.RedatorUpdateDTO;
import com.example.gad.models.projection.RedatorProjection;
import com.example.gad.repositories.RedatorRepository;
import com.example.gad.services.exceptions.DataBindingViolationException;
import com.example.gad.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedatorService {

    private final RedatorRepository redatorRepository;

    private final UsuarioService usuarioService;

    public List<RedatorProjection> findAll() {
        return this.redatorRepository.findAllProjectedBy();
    }

    public Redator findById(UUID id) {

        return this.redatorRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(
                        "Redator nao encontrado! Id: " + id
                ));
    }

    public Redator fromDTO(RedatorCreateDTO objDto) {
        Redator redator = new Redator();
        Usuario usuario = new Usuario();
        usuario.setId(objDto.getUsuarioId());
        redator.setUsuario(usuario);
        redator.setEmpresaId(objDto.getEmpresaId());
        return redator;
    }

    @Transactional
    public Redator create(Redator obj) {

        if (obj.getUsuario() == null || obj.getUsuario().getId() == null) {

            throw new IllegalArgumentException(
                    "Usuario deve ser informado para criar redator!"
            );
        }

        Usuario usuario = this.usuarioService.findById(
                obj.getUsuario().getId()
        );

        obj.setUsuario(usuario);

        return this.redatorRepository.save(obj);
    }

    @Transactional
    public Redator update(UUID id, RedatorUpdateDTO objDto) {

        Redator newObj = findById(id);

        if (objDto.getEmpresaId() != null) {
            newObj.setEmpresaId(objDto.getEmpresaId());
        }

        return this.redatorRepository.save(newObj);
    }

    @Transactional
    public void delete(UUID id) {

        findById(id);

        try {

            this.redatorRepository.deleteById(id);

        } catch (Exception e) {

            throw new DataBindingViolationException(
                    "Nao e possivel excluir: existem entidades relacionadas!"
            );
        }
    }
}
