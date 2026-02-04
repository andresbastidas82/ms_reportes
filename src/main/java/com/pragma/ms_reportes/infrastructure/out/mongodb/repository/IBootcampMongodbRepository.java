package com.pragma.ms_reportes.infrastructure.out.mongodb.repository;

import com.pragma.ms_reportes.infrastructure.out.mongodb.document.BootcampDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IBootcampMongodbRepository extends ReactiveMongoRepository<BootcampDocument, String> {

    Mono<BootcampDocument> findByBootcampId(Long bootcampId);
}
