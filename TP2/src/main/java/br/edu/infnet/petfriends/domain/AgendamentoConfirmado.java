package br.edu.infnet.petfriends.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/** Evento concreto emitido quando um atendimento passa a estar confirmado. */
public record AgendamentoConfirmado(
        UUID eventoId,
        Instant ocorridoEm,
        UUID agendamentoId,
        TutorId tutorId,
        PetId petId,
        LocalDateTime horario
) implements EventoDominio {

    public AgendamentoConfirmado {
        Objects.requireNonNull(eventoId);
        Objects.requireNonNull(ocorridoEm);
        Objects.requireNonNull(agendamentoId);
        Objects.requireNonNull(tutorId);
        Objects.requireNonNull(petId);
        Objects.requireNonNull(horario);
    }

    public static AgendamentoConfirmado agora(
            UUID agendamentoId, TutorId tutorId, PetId petId, LocalDateTime horario) {
        return new AgendamentoConfirmado(
                UUID.randomUUID(), Instant.now(), agendamentoId, tutorId, petId, horario);
    }

    @Override
    public String tipo() {
        return "agendamento.confirmado";
    }
}
