package com.pragma.ms_reportes.application.dto;

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
public class GeneralResponse <T>{
    private String message;
    private Boolean isSuccess;
    private T data;
}
