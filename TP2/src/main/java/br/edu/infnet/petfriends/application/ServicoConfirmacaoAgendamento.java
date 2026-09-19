package br.edu.infnet.petfriends.application;

import br.edu.infnet.petfriends.domain.Agendamento;

public final class ServicoConfirmacaoAgendamento {
    private final PublicadorEventos publicador;

    public ServicoConfirmacaoAgendamento(PublicadorEventos publicador) {
        this.publicador = publicador;
    }

    public void confirmar(Agendamento agendamento) {
        agendamento.confirmar();
        agendamento.removerEventosPendentes().forEach(publicador::publicar);
    }
}
