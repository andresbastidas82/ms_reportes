package com.pragma.ms_reportes.domain.usecase;

import com.pragma.ms_reportes.domain.api.IBootcampServicePort;
import com.pragma.ms_reportes.domain.exception.BootcampAlreadyExistsException;
import com.pragma.ms_reportes.domain.exception.NotFoundException;
import com.pragma.ms_reportes.domain.model.Bootcamp;
import com.pragma.ms_reportes.domain.spi.IBootcampPersistencePort;
import com.pragma.ms_reportes.domain.spi.IPersonClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BootcampUseCase implements IBootcampServicePort {

    private final IBootcampPersistencePort bootcampPersistencePort;
    private final IPersonClientPort personClientPort;


    @Override
    public Mono<Bootcamp> createOrUpdateBootcamp(Bootcamp bootcamp) {
        return bootcampPersistencePort.getBootcampById(bootcamp.getBootcampId())
                .flatMap(existing ->
                        Mono.<Bootcamp>error(
                                new BootcampAlreadyExistsException(
                                        "El bootcamp con id: " + bootcamp.getBootcampId() + " ya esta registrado."
                                )
                        )
                )
                .switchIfEmpty(
                        Mono.defer(() -> bootcampPersistencePort.createOrUpdateBootcamp(bootcamp))
                );
    }

    @Override
    public Mono<Boolean> registerPersonInBootcamp(Long bootcampId, Long personId) {
        return personClientPort.getPersonById(personId)
                .switchIfEmpty(Mono.error(new NotFoundException("Persona no encontrada")))
                .flatMap(person ->
                        bootcampPersistencePort.getBootcampById(bootcampId)
                                .switchIfEmpty(Mono.error(new NotFoundException("Bootcamp no encontrado")))
                                .flatMap(bootcamp -> {
                                    if (bootcamp.getPersons() == null) {
                                        bootcamp.setPersons(new ArrayList<>());
                                    }
                                    bootcamp.getPersons().add(person);
                                    return bootcampPersistencePort.createOrUpdateBootcamp(bootcamp);
                                })
                )
                .thenReturn(true);
    }

    @Override
    public Flux<Bootcamp> findTopBootcamps(int limit) {
        return bootcampPersistencePort.findTopBootcamps(limit);
    }


}
