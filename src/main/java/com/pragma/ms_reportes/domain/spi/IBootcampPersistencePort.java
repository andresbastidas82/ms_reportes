package com.pragma.ms_reportes.domain.spi;

import com.pragma.ms_reportes.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampPersistencePort {

    Mono<Bootcamp> createOrUpdateBootcamp(Bootcamp bootcamp);

    Mono<Bootcamp> getBootcampById(Long id);

}
