package com.pragma.ms_reportes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    private Long id;
    private String name;
    private String email;
    private String identificationNumber;
}
