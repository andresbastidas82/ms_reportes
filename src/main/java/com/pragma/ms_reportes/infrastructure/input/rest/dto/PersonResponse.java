package com.pragma.ms_reportes.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse {
    private Long id;
    private String name;
    private String identificationNumber;
    private String email;
}
