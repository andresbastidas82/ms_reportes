package com.pragma.ms_reportes.infrastructure.out.mongodb.adapter;

import com.pragma.ms_reportes.domain.model.Bootcamp;
import com.pragma.ms_reportes.domain.spi.IBootcampPersistencePort;
import com.pragma.ms_reportes.infrastructure.out.mongodb.mapper.IBootcampDocumentMapper;
import com.pragma.ms_reportes.infrastructure.out.mongodb.repository.IBootcampMongodbRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
public class BootcampRepositoryAdapter implements IBootcampPersistencePort {

    private final IBootcampMongodbRepository bootcampMongodbRepository;
    private final IBootcampDocumentMapper bootcampDocumentMapper;

    public BootcampRepositoryAdapter(IBootcampMongodbRepository bootcampMongodbRepository,
                                     IBootcampDocumentMapper bootcampDocumentMapper) {
        this.bootcampMongodbRepository = bootcampMongodbRepository;
        this.bootcampDocumentMapper = bootcampDocumentMapper;
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
}
