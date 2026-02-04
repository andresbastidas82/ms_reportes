package com.pragma.ms_reportes.infrastructure.out.mongodb.adapter;

import com.pragma.ms_reportes.domain.model.Bootcamp;
import com.pragma.ms_reportes.domain.spi.IBootcampPersistencePort;
import com.pragma.ms_reportes.infrastructure.out.mongodb.document.BootcampDocument;
import com.pragma.ms_reportes.infrastructure.out.mongodb.mapper.IBootcampDocumentMapper;
import com.pragma.ms_reportes.infrastructure.out.mongodb.repository.IBootcampMongodbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
@Slf4j
public class BootcampRepositoryAdapter implements IBootcampPersistencePort {

    private final IBootcampMongodbRepository bootcampMongodbRepository;
    private final IBootcampDocumentMapper bootcampDocumentMapper;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public BootcampRepositoryAdapter(IBootcampMongodbRepository bootcampMongodbRepository,
                                     IBootcampDocumentMapper bootcampDocumentMapper,
                                     ReactiveMongoTemplate reactiveMongoTemplate) {
        this.bootcampMongodbRepository = bootcampMongodbRepository;
        this.bootcampDocumentMapper = bootcampDocumentMapper;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @Transactional
    @Override
    public Mono<Bootcamp> createOrUpdateBootcamp(Bootcamp bootcamp) {
        return bootcampMongodbRepository
                .save(bootcampDocumentMapper.toDocument(bootcamp))
                .map(bootcampDocumentMapper::toModel);
    }

    @Override
    public Mono<Bootcamp> getBootcampById(Long id) {
        return bootcampMongodbRepository.findByBootcampId(id)
                .map(bootcampDocumentMapper::toModel);
    }

    @Override
    public Flux<Bootcamp> findTopBootcamps(int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.addFields()
                        .addField("personsCount")
                        .withValue(
                                ArrayOperators.Size.lengthOfArray(
                                        ConditionalOperators.ifNull("persons")
                                                .then(Collections.emptyList())
                                )
                        )
                        .build(),
                Aggregation.sort(Sort.by(Sort.Direction.DESC, "personsCount")),
                Aggregation.limit(limit)
        );

        return reactiveMongoTemplate.aggregate(aggregation, "bootcamps", BootcampDocument.class)
                .doOnNext(result -> log.info("Bootcamp encontrado: {}", result.getBootcampId()))
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontraron bootcamps")))
                .map(bootcampDocumentMapper::toModel);
    }
}
