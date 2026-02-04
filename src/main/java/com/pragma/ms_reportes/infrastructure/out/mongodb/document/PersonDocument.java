package com.pragma.ms_reportes.infrastructure.out.mongodb.document;

import lombok.Data;

@Data
public class PersonDocument {
    private Long id;
    private String name;
    private String email;
    private String identificationNumber;
}
