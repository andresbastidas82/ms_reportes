package com.pragma.ms_reportes.domain.usecase;

import com.pragma.ms_reportes.domain.exception.BootcampAlreadyExistsException;
import com.pragma.ms_reportes.domain.model.Bootcamp;
import com.pragma.ms_reportes.domain.model.Person;
import com.pragma.ms_reportes.domain.spi.IBootcampPersistencePort;
import com.pragma.ms_reportes.domain.spi.IPersonClientPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampUseCaseTest {

    @Mock
    private IBootcampPersistencePort bootcampPersistencePort;

    @Mock
    private IPersonClientPort personClientPort;

    @InjectMocks
    private BootcampUseCase bootcampUseCase;

    // --- TESTS PARA createOrUpdateBootcamp ---

    @Test
    @DisplayName("Create Bootcamp: Should save when bootcamp does not exist")
    void createOrUpdateBootcamp_WhenNotExists_ShouldSave() {
        // Arrange
        Long bootcampId = 1L;
        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setBootcampId(bootcampId);
        bootcamp.setName("Java Bootcamp");

        // Simulamos que NO existe (Mono.empty)
        when(bootcampPersistencePort.getBootcampById(bootcampId)).thenReturn(Mono.empty());
        // Simulamos el guardado exitoso
        when(bootcampPersistencePort.createOrUpdateBootcamp(bootcamp)).thenReturn(Mono.just(bootcamp));

        // Act
        Mono<Bootcamp> result = bootcampUseCase.createOrUpdateBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectNext(bootcamp)
                .verifyComplete();

        verify(bootcampPersistencePort).getBootcampById(bootcampId);
        verify(bootcampPersistencePort).createOrUpdateBootcamp(bootcamp);
    }

    @Test
    @DisplayName("Create Bootcamp: Should throw exception when bootcamp already exists")
    void createOrUpdateBootcamp_WhenExists_ShouldThrowException() {
        // Arrange
        Long bootcampId = 1L;
        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setBootcampId(bootcampId);

        // Simulamos que YA existe
        when(bootcampPersistencePort.getBootcampById(bootcampId)).thenReturn(Mono.just(bootcamp));

        // Act
        Mono<Bootcamp> result = bootcampUseCase.createOrUpdateBootcamp(bootcamp);

        // Assert
        StepVerifier.create(result)
                .expectError(BootcampAlreadyExistsException.class)
                .verify();

        // Verificamos que NUNCA se llame a guardar
        verify(bootcampPersistencePort, never()).createOrUpdateBootcamp(any());
    }

    // --- TESTS PARA registerPersonInBootcamp ---

    @Test
    @DisplayName("Register Person: Should succeed when Person and Bootcamp exist (List was null)")
    void registerPerson_WhenAllExistsAndListNull_ShouldSuccess() {
        // Arrange
        Long bootcampId = 100L;
        Long personId = 1L;

        Person person = new Person(); // Asumiendo modelo Person
        person.setId(personId);

        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setBootcampId(bootcampId);
        bootcamp.setPersons(null); // Caso: lista nula

        when(personClientPort.getPersonById(personId)).thenReturn(Mono.just(person));
        when(bootcampPersistencePort.getBootcampById(bootcampId)).thenReturn(Mono.just(bootcamp));
        when(bootcampPersistencePort.createOrUpdateBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcamp));

        // Act
        Mono<Boolean> result = bootcampUseCase.registerPersonInBootcamp(bootcampId, personId);

        // Assert
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(bootcampPersistencePort).createOrUpdateBootcamp(argThat(b ->
                b.getPersons() != null && b.getPersons().size() == 1
        ));
    }

    @Test
    @DisplayName("Register Person: Should throw NotFoundException when Person does not exist")
    void registerPerson_WhenPersonNotFound_ShouldThrowException() {
        // Arrange
        Long bootcampId = 100L;
        Long personId = 999L;

        when(personClientPort.getPersonById(personId)).thenReturn(Mono.empty());

        // Act
        Mono<Boolean> result = bootcampUseCase.registerPersonInBootcamp(bootcampId, personId);

        // Assert
        StepVerifier.create(result)
                .expectErrorMessage("Persona no encontrada") // Verifica el mensaje exacto
                .verify();

        verify(bootcampPersistencePort, never()).getBootcampById(any());
        verify(bootcampPersistencePort, never()).createOrUpdateBootcamp(any());
    }

    @Test
    @DisplayName("Register Person: Should throw NotFoundException when Bootcamp does not exist")
    void registerPerson_WhenBootcampNotFound_ShouldThrowException() {
        // Arrange
        Long bootcampId = 999L;
        Long personId = 1L;
        Person person = new Person();

        when(personClientPort.getPersonById(personId)).thenReturn(Mono.just(person));
        when(bootcampPersistencePort.getBootcampById(bootcampId)).thenReturn(Mono.empty());

        // Act
        Mono<Boolean> result = bootcampUseCase.registerPersonInBootcamp(bootcampId, personId);

        // Assert
        StepVerifier.create(result)
                .expectErrorMessage("Bootcamp no encontrado")
                .verify();

        verify(bootcampPersistencePort, never()).createOrUpdateBootcamp(any());
    }

    // --- TESTS PARA findTopBootcamps ---

    @Test
    @DisplayName("Find Top Bootcamps: Should return flux of bootcamps")
    void findTopBootcamps_ShouldReturnFlux() {
        // Arrange
        int limit = 5;
        Bootcamp b1 = new Bootcamp();
        Bootcamp b2 = new Bootcamp();

        when(bootcampPersistencePort.findTopBootcamps(limit)).thenReturn(Flux.just(b1, b2));

        // Act
        Flux<Bootcamp> result = bootcampUseCase.findTopBootcamps(limit);

        // Assert
        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        verify(bootcampPersistencePort).findTopBootcamps(limit);
    }
}