package br.edu.infnet.petfriends.domain;

import java.util.Objects;
import java.util.UUID;

/** Identidade do agregado Tutor, sem carregar uma instância desse agregado. */
public record TutorId(UUID valor) {
    public TutorId {
        Objects.requireNonNull(valor, "O ID do tutor é obrigatório");
    }
}
