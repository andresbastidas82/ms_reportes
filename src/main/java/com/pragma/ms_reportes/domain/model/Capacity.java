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
public class Capacity {
    private Long id;
    private String name;

    private List<Technology> technologies;
}
