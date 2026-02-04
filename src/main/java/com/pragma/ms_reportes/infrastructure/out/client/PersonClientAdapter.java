package com.pragma.ms_reportes.infrastructure.out.client;

import com.pragma.ms_reportes.domain.exceptions.NotFoundException;
import com.pragma.ms_reportes.domain.model.Person;
import com.pragma.ms_reportes.domain.spi.IPersonClientPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PersonClientAdapter implements IPersonClientPort {

    private final WebClient webClient;

    public PersonClientAdapter(@Qualifier("personWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<Person> getPersonById(Long id) {
        return webClient.get()
                .uri("/person/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new NotFoundException("Persona no encontrada")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("Error en el servicio de personas")))
                .bodyToMono(Person.class);
    }
}
