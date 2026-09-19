package br.edu.infnet.petfriends.application;

import br.edu.infnet.petfriends.domain.EventoDominio;

@FunctionalInterface
public interface PublicadorEventos {
    void publicar(EventoDominio evento);
}
