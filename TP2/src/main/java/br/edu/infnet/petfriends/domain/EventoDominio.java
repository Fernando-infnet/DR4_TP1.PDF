package br.edu.infnet.petfriends.domain;

import java.time.Instant;
import java.util.UUID;

/** Contrato comum a todos os fatos relevantes ocorridos no domínio. */
public interface EventoDominio {
    UUID eventoId();

    Instant ocorridoEm();

    String tipo();
}
