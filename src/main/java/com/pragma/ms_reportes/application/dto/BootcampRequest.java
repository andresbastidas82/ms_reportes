package com.pragma.ms_reportes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BootcampRequest {
    private Long id;
    private String name;
    private String description;

    private List<CapacityRequest> capacities;
}
