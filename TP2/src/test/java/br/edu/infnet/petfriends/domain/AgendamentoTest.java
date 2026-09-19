package br.edu.infnet.petfriends.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AgendamentoTest {
    @Test
    void deveConfirmarERegistrarEvento() {
        Agendamento agendamento = novoAgendamento();

        agendamento.confirmar();

        assertEquals(Agendamento.Status.CONFIRMADO, agendamento.status());
        EventoDominio evento = assertInstanceOf(
                AgendamentoConfirmado.class,
                agendamento.removerEventosPendentes().getFirst());
        assertEquals("agendamento.confirmado", evento.tipo());
    }

    @Test
    void naoDeveConfirmarDuasVezes() {
        Agendamento agendamento = novoAgendamento();
        agendamento.confirmar();

        assertThrows(IllegalStateException.class, agendamento::confirmar);
    }

    private static Agendamento novoAgendamento() {
        return new Agendamento(
                UUID.randomUUID(),
                new TutorId(UUID.randomUUID()),
                new PetId(UUID.randomUUID()),
                LocalDateTime.now().plusDays(2));
    }
}
