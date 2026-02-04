package com.pragma.ms_reportes.domain.api;

import com.pragma.ms_reportes.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {

    Mono<Bootcamp> createOrUpdateBootcamp(Bootcamp bootcamp);

    Mono<Boolean> registerPersonInBootcamp(Long bootcampId, Long personId);
}
