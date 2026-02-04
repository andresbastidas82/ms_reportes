package com.pragma.ms_reportes.domain.spi;

import com.pragma.ms_reportes.domain.model.Person;
import reactor.core.publisher.Mono;

public interface IPersonClientPort {

    Mono<Person> getPersonById(Long id);
}
