package com.example.gad.services;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.gad.models.Cliente;
import com.example.gad.models.Usuario;
import com.example.gad.repositories.ClienteRepository;
import com.example.gad.services.exceptions.DataBindingViolationException;
import com.example.gad.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente findById(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new ObjectNotFoundException(
                                "Cliente não encontrado. Id: " + id
                        )
                );
    }

    @Transactional
    public Cliente create(Cliente cliente) {

        if (cliente.getUsuario() == null ||
                cliente.getUsuario().getId() == null) {

            throw new DataBindingViolationException(
                    "Usuário não informado"
            );
        }

        Usuario usuario = usuarioService.findById(
                cliente.getUsuario().getId()
        );

        if (clienteRepository.existsById(usuario.getId())) {
            throw new DataBindingViolationException(
                    "Já existe um cliente para este usuário"
            );
        }

        cliente.setUsuario(usuario);

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente update(Cliente obj) {

        Cliente cliente = findById(obj.getUsuarioId());

        if (obj.getEmpresaId() != null &&
                !obj.getEmpresaId().isBlank()) {

            cliente.setEmpresaId(obj.getEmpresaId());
        }

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void delete(UUID id) {

        findById(id);

        try {

            clienteRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new DataBindingViolationException(
                    "Não é possível excluir: existem entidades relacionadas"
            );
        }
    }
}