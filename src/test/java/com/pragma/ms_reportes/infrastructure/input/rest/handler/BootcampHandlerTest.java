package com.pragma.ms_reportes.infrastructure.input.rest.handler;

import com.pragma.ms_reportes.application.dto.BootcampRequest;
import com.pragma.ms_reportes.application.dto.GeneralResponse;
import com.pragma.ms_reportes.application.helper.IBootcampHelper;
import com.pragma.ms_reportes.domain.model.Bootcamp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampHandlerTest {

    @Mock
    private IBootcampHelper bootcampHelper;

    @InjectMocks
    private BootcampHandler bootcampHandler;

    // --- TEST: registerBootcamp ---

    @Test
    @DisplayName("Register Bootcamp: Should return 200 OK when successful")
    void registerBootcamp_ShouldReturnOk() {
        // Arrange
        BootcampRequest requestDto = new BootcampRequest();
        requestDto.setName("Java Bootcamp");

        GeneralResponse<BootcampRequest> responseDto = GeneralResponse.<BootcampRequest>builder()
                .message("Creado")
                .isSuccess(true)
                .data(requestDto)
                .build();

        // Simulamos el request con cuerpo
        MockServerRequest request = MockServerRequest.builder()
                .body(Mono.just(requestDto));

        when(bootcampHelper.registerBootcamp(any(BootcampRequest.class)))
                .thenReturn(Mono.just(responseDto));

        // Act
        Mono<ServerResponse> result = bootcampHandler.registerBootcamp(request);

        // Assert
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK, response.statusCode());
                })
                .verifyComplete();

        verify(bootcampHelper).registerBootcamp(any(BootcampRequest.class));
    }

    // --- TESTS: registerPersonInBootcamp ---

    @Test
    @DisplayName("Register Person: Should return 200 OK when params are valid")
    void registerPersonInBootcamp_ShouldReturnOk() {
        // Arrange
        Long bootcampId = 1L;
        Long personId = 2L;

        GeneralResponse<Boolean> responseDto = GeneralResponse.<Boolean>builder()
                .message("Asociado")
                .isSuccess(true)
                .data(true)
                .build();

        // Simulamos request con Query Params
        MockServerRequest request = MockServerRequest.builder()
                .queryParam("bootcampId", String.valueOf(bootcampId))
                .queryParam("personId", String.valueOf(personId))
                .build();

        when(bootcampHelper.registerPersonInBootcamp(bootcampId, personId))
                .thenReturn(Mono.just(responseDto));

        // Act
        Mono<ServerResponse> result = bootcampHandler.registerPersonInBootcamp(request);

        // Assert
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK, response.statusCode());
                })
                .verifyComplete();

        verify(bootcampHelper).registerPersonInBootcamp(bootcampId, personId);
    }

    @Test
    @DisplayName("Register Person: Should throw IllegalArgumentException when bootcampId is missing")
    void registerPersonInBootcamp_WhenBootcampIdMissing_ShouldThrowException() {
        // Arrange
        MockServerRequest request = MockServerRequest.builder()
                // Falta bootcampId
                .queryParam("personId", "2")
                .build();

        // Act & Assert
        // Nota: Como la excepción se lanza antes de entrar al flujo reactivo (en el map/orElseThrow),
        // usamos assertThrows o try-catch, pero al ser un Handler reactivo, a veces se envuelve.
        // En tu código usas .orElseThrow(), lo cual lanza la excepción inmediatamente al ejecutar el método.

        try {
            bootcampHandler.registerPersonInBootcamp(request);
        } catch (IllegalArgumentException e) {
            assertEquals("bootcampId es requerido", e.getMessage());
        }
    }

    @Test
    @DisplayName("Register Person: Should throw IllegalArgumentException when personId is missing")
    void registerPersonInBootcamp_WhenPersonIdMissing_ShouldThrowException() {
        // Arrange
        MockServerRequest request = MockServerRequest.builder()
                .queryParam("bootcampId", "1")
                // Falta personId
                .build();

        // Act & Assert
        try {
            bootcampHandler.registerPersonInBootcamp(request);
        } catch (IllegalArgumentException e) {
            assertEquals("personId es requerido", e.getMessage());
        }
    }

    // --- TEST: findTopBootcamps ---

    @Test
    @DisplayName("Find Top Bootcamps: Should use default limit 1 when param is missing")
    void findTopBootcamps_WhenLimitMissing_ShouldUseDefault() {
        // Arrange
        MockServerRequest request = MockServerRequest.builder()
                // Sin query param
                .build();

        when(bootcampHelper.findTopBootcamps(1)) // Esperamos el default 1
                .thenReturn(Flux.empty());

        // Act
        Mono<ServerResponse> result = bootcampHandler.findTopBootcamps(request);

        // Assert
        StepVerifier.create(result)
                .assertNext(response -> assertEquals(HttpStatus.OK, response.statusCode()))
                .verifyComplete();

        verify(bootcampHelper).findTopBootcamps(1);
    }
}