package br.edu.infnet.petfriends.domain;

import java.util.Objects;
import java.util.UUID;

public record PetId(UUID valor) {
    public PetId {
        Objects.requireNonNull(valor, "O ID do pet é obrigatório");
    }
}
