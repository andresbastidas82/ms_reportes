package com.pragma.ms_reportes.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bootcamp {
    private String id;
    private Long bootcampId;
    private String name;
    private String description;

    private List<Capacity> capacities;
    private List<Person> persons;
}
