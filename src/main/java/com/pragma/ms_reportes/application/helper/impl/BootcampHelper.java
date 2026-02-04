package com.pragma.ms_reportes.application.helper.impl;

import com.pragma.ms_reportes.application.dto.BootcampRequest;
import com.pragma.ms_reportes.application.dto.BootcampResponse;
import com.pragma.ms_reportes.application.dto.GeneralResponse;
import com.pragma.ms_reportes.application.helper.IBootcampHelper;
import com.pragma.ms_reportes.application.mapper.IBootcampRequestMapper;
import com.pragma.ms_reportes.domain.api.IBootcampServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BootcampHelper implements IBootcampHelper {

    private final IBootcampServicePort bootcampServicePort;
    private final IBootcampRequestMapper bootcampRequestMapper;

    @Override
    public Mono<GeneralResponse> registerBootcamp(BootcampRequest request) {
        return bootcampServicePort
                .createOrUpdateBootcamp(bootcampRequestMapper.toModel(request))
                .map(result -> GeneralResponse.builder()
                        .isSuccess(true)
                        .data(result)
                        .message("Bootcamp registrado correctamente")
                        .build());
    }

    @Override
    public Mono<GeneralResponse> registerPersonInBootcamp(Long bootcampId, Long personId) {
        return bootcampServicePort.registerPersonInBootcamp(bootcampId, personId)
                .map(result -> GeneralResponse.builder()
                        .isSuccess(true)
                        .message("Persona registrada en el bootcamp")
                        .build()
                );
    }

    @Override
    public Flux<BootcampResponse> findTopBootcamps(int limit) {
        return bootcampServicePort.findTopBootcamps(limit)
                .map(bootcampRequestMapper::toBootcampResponse);
    }
}
