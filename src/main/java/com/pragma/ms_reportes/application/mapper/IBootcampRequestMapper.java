package com.pragma.ms_reportes.application.mapper;

import com.pragma.ms_reportes.application.dto.BootcampRequest;
import com.pragma.ms_reportes.application.dto.BootcampResponse;
import com.pragma.ms_reportes.domain.model.Bootcamp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IBootcampRequestMapper {

    @Mapping(source = "id", target = "bootcampId")
    @Mapping(target = "id", ignore = true)
    Bootcamp toModel(BootcampRequest request);

    BootcampResponse toBootcampResponse(Bootcamp bootcamp);
}
