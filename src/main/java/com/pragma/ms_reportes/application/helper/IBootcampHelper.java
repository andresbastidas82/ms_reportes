package com.pragma.ms_reportes.application.helper;

import com.pragma.ms_reportes.application.dto.BootcampRequest;
import com.pragma.ms_reportes.application.dto.GeneralResponse;
import reactor.core.publisher.Mono;

public interface IBootcampHelper {

    Mono<GeneralResponse> registerBootcamp(BootcampRequest request);
    Mono<GeneralResponse> registerPersonInBootcamp(Long bootcampId, Long personId);
}
