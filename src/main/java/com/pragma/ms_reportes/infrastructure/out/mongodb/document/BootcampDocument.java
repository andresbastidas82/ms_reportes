package com.pragma.ms_reportes.infrastructure.out.mongodb.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "bootcamps")
public class BootcampDocument {

    @Id
    private String id;

    private Long bootcampId;
    private String name;
    private String description;

    private List<CapacityDocument> capacities;
    private List<PersonDocument> persons;

}
