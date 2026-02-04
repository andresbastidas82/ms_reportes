package com.pragma.ms_reportes.infrastructure.input.rest.handler;

import com.pragma.ms_reportes.application.dto.BootcampRequest;
import com.pragma.ms_reportes.application.dto.GeneralResponse;
import com.pragma.ms_reportes.application.helper.IBootcampHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BootcampHandler {

    private final IBootcampHelper bootcampHelper;

    public Mono<ServerResponse> registerBootcamp(ServerRequest request) {
        return request.bodyToMono(BootcampRequest.class)
                .flatMap(bootcampRequest -> ServerResponse.ok()
                        .body(bootcampHelper.registerBootcamp(bootcampRequest), GeneralResponse.class));
    }

    public Mono<ServerResponse> registerPersonInBootcamp(ServerRequest request) {
        Long bootcampId = request.queryParam("bootcampId")
                .map(Long::valueOf)
                .orElseThrow(() -> new IllegalArgumentException("bootcampId es requerido"));

        Long personId = request.queryParam("personId")
                .map(Long::valueOf)
                .orElseThrow(() -> new IllegalArgumentException("personId es requerido"));

        return bootcampHelper.registerPersonInBootcamp(bootcampId, personId)
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));

    }

    public Mono<ServerResponse> findTopBootcamps(ServerRequest request) {
        int limit = Integer.parseInt(request.queryParam("limit").orElse("1"));

        return bootcampHelper.findTopBootcamps(limit)
                .collectList()
                .flatMap(response ->
                        ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));
    }

}
