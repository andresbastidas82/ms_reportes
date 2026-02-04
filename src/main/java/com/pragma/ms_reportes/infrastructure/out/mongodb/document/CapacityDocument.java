package com.pragma.ms_reportes.infrastructure.out.mongodb.document;

import lombok.Data;

import java.util.List;

@Data
public class CapacityDocument {
    private Long id;
    private String name;
    private List<TechnologyDocument> technologies;
}
