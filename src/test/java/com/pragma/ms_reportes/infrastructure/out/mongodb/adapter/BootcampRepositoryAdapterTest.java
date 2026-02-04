package com.pragma.ms_reportes.infrastructure.out.mongodb.adapter;

import com.pragma.ms_reportes.domain.model.Bootcamp;
import com.pragma.ms_reportes.infrastructure.out.mongodb.document.BootcampDocument;
import com.pragma.ms_reportes.infrastructure.out.mongodb.mapper.IBootcampDocumentMapper;
import com.pragma.ms_reportes.infrastructure.out.mongodb.repository.IBootcampMongodbRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BootcampRepositoryAdapterTest {

    @Mock
    private IBootcampMongodbRepository bootcampMongodbRepository;

    @Mock
    private IBootcampDocumentMapper bootcampDocumentMapper;

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private BootcampRepositoryAdapter bootcampRepositoryAdapter;

    @Test
    @DisplayName("CreateOrUpdate: Should map to document, save and map back to model")
    void createOrUpdateBootcamp_ShouldSaveAndReturnModel() {
        // Arrange
        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setBootcampId(1L);
        bootcamp.setName("Java");

        BootcampDocument document = new BootcampDocument();
        document.setBootcampId(1L);
        document.setName("Java");

        // 1. Mapeo de Modelo a Documento
        when(bootcampDocumentMapper.toDocument(bootcamp)).thenReturn(document);
        // 2. Guardado en Repositorio
        when(bootcampMongodbRepository.save(document)).thenReturn(Mono.just(document));
        // 3. Mapeo de Documento a Modelo (retorno)
        when(bootcampDocumentMapper.toModel(document)).thenReturn(bootcamp);

        // Act
        Mono<Bootcamp> result = bootcampRepositoryAdapter.createOrUpdateBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectNext(bootcamp)
                .verifyComplete();

        verify(bootcampDocumentMapper).toDocument(bootcamp);
        verify(bootcampMongodbRepository).save(document);
        verify(bootcampDocumentMapper).toModel(document);
    }

    @Test
    @DisplayName("GetById: Should return bootcamp when found")
    void getBootcampById_WhenFound_ShouldReturnBootcamp() {
        // Arrange
        Long id = 1L;
        BootcampDocument document = new BootcampDocument();
        document.setBootcampId(id);

        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setBootcampId(id);

        when(bootcampMongodbRepository.findByBootcampId(id)).thenReturn(Mono.just(document));
        when(bootcampDocumentMapper.toModel(document)).thenReturn(bootcamp);

        // Act
        Mono<Bootcamp> result = bootcampRepositoryAdapter.getBootcampById(id);

        // Assert
        StepVerifier.create(result)
                .expectNext(bootcamp)
                .verifyComplete();

        verify(bootcampMongodbRepository).findByBootcampId(id);
    }

    @Test
    @DisplayName("GetById: Should return empty when not found")
    void getBootcampById_WhenNotFound_ShouldReturnEmpty() {
        // Arrange
        Long id = 99L;
        when(bootcampMongodbRepository.findByBootcampId(id)).thenReturn(Mono.empty());

        // Act
        Mono<Bootcamp> result = bootcampRepositoryAdapter.getBootcampById(id);

        // Assert
        StepVerifier.create(result)
                .verifyComplete(); // Espera completarse sin emitir elementos
    }

    @Test
    @DisplayName("FindTopBootcamps: Should return aggregated results")
    void findTopBootcamps_ShouldReturnFlux() {
        // Arrange
        int limit = 5;
        BootcampDocument doc1 = new BootcampDocument();
        doc1.setBootcampId(1L);
        BootcampDocument doc2 = new BootcampDocument();
        doc2.setBootcampId(2L);

        Bootcamp b1 = new Bootcamp();
        b1.setBootcampId(1L);
        Bootcamp b2 = new Bootcamp();
        b2.setBootcampId(2L);

        // Mock del ReactiveMongoTemplate.aggregate
        // Usamos any(Aggregation.class) porque construir el objeto Aggregation exacto para el equals es complejo
        when(reactiveMongoTemplate.aggregate(any(Aggregation.class), eq("bootcamps"), eq(BootcampDocument.class)))
                .thenReturn(Flux.just(doc1, doc2));

        when(bootcampDocumentMapper.toModel(doc1)).thenReturn(b1);
        when(bootcampDocumentMapper.toModel(doc2)).thenReturn(b2);

        // Act
        Flux<Bootcamp> result = bootcampRepositoryAdapter.findTopBootcamps(limit);

        // Assert
        StepVerifier.create(result)
                .expectNext(b1)
                .expectNext(b2)
                .verifyComplete();

        verify(reactiveMongoTemplate).aggregate(any(Aggregation.class), eq("bootcamps"), eq(BootcampDocument.class));
    }

    @Test
    @DisplayName("FindTopBootcamps: Should throw RuntimeException when result is empty")
    void findTopBootcamps_WhenEmpty_ShouldThrowException() {
        // Arrange
        int limit = 5;

        // Simulamos que la agregación no devuelve nada
        when(reactiveMongoTemplate.aggregate(any(Aggregation.class), eq("bootcamps"), eq(BootcampDocument.class)))
                .thenReturn(Flux.empty());

        // Act
        Flux<Bootcamp> result = bootcampRepositoryAdapter.findTopBootcamps(limit);

        // Assert
        StepVerifier.create(result)
                .expectErrorMessage("No se encontraron bootcamps")
                .verify();
    }
}