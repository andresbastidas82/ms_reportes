package com.pragma.ms_reportes.infrastructure.input.rest.router;

import com.pragma.ms_reportes.application.dto.BootcampRequest;
import com.pragma.ms_reportes.infrastructure.input.rest.handler.BootcampHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BootcampRouter {

    @Bean
    @RouterOperations({

            @RouterOperation(
                    path = "/api/v1/report-service",
                    method = RequestMethod.POST,
                    beanClass = BootcampHandler.class,
                    beanMethod = "registerBootcamp",
                    operation = @Operation(
                            operationId = "registerBootcamp",
                            summary = "Registrar un bootcamp",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = BootcampRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Bootcamp creado")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/report-service",
                    method = RequestMethod.GET,
                    beanClass = BootcampHandler.class,
                    beanMethod = "registerPersonInBootcamp",
                    operation = @Operation(
                            operationId = "registerPersonInBootcamp",
                            summary = "Registrar persona en bootcamp",
                            parameters = {
                                    @Parameter(name = "bootcampId", description = "ID del bootcamp", required = true),
                                    @Parameter(name = "personId", description = "ID de la persona", required = true)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Persona registrada en bootcamp")
                            }
                    )
            ),

            @RouterOperation(
                    path = "/api/v1/report-service/top",
                    method = RequestMethod.GET,
                    beanClass = BootcampHandler.class,
                    beanMethod = "findTopBootcamps",
                    operation = @Operation(
                            operationId = "findTopBootcamps",
                            summary = "Obtener bootcamps más exitosos",
                            parameters = {
                                    @Parameter(name = "limit", description = "Cantidad de bootcamps a retornar", required = true)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Lista de bootcamps exitosos")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> bootcampRoutes(BootcampHandler handler) {
        return route(POST("/api/v1/report-service"), handler::registerBootcamp)
                .andRoute(GET("/api/v1/report-service"), handler::registerPersonInBootcamp)
                .andRoute(GET("/api/v1/report-service/top"), handler::findTopBootcamps);
    }
}
