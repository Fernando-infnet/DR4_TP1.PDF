package br.edu.infnet.petfriends.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Raiz do agregado Agendamento. */
public final class Agendamento {
    private final UUID id;
    private final TutorId tutorId;
    private final PetId petId;
    private final LocalDateTime horario;
    private Status status;
    private final List<EventoDominio> eventos = new ArrayList<>();

    public Agendamento(UUID id, TutorId tutorId, PetId petId, LocalDateTime horario) {
        this.id = Objects.requireNonNull(id, "O ID é obrigatório");
        this.tutorId = Objects.requireNonNull(tutorId, "O tutor é obrigatório");
        this.petId = Objects.requireNonNull(petId, "O pet é obrigatório");
        this.horario = Objects.requireNonNull(horario, "O horário é obrigatório");
        if (!horario.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("O agendamento deve ser feito para uma data futura");
        }
        this.status = Status.PENDENTE;
    }

    /** Método de negócio que protege a invariante e prevê a publicação do evento. */
    public void confirmar() {
        if (status != Status.PENDENTE) {
            throw new IllegalStateException("Somente agendamentos pendentes podem ser confirmados");
        }
        status = Status.CONFIRMADO;
        eventos.add(AgendamentoConfirmado.agora(id, tutorId, petId, horario));
    }

    public List<EventoDominio> removerEventosPendentes() {
        List<EventoDominio> pendentes = List.copyOf(eventos);
        eventos.clear();
        return pendentes;
    }

    public UUID id() {
        return id;
    }

    public TutorId tutorId() {
        return tutorId;
    }

    public PetId petId() {
        return petId;
    }

    public Status status() {
        return status;
    }

    public enum Status { PENDENTE, CONFIRMADO, CANCELADO }
}
