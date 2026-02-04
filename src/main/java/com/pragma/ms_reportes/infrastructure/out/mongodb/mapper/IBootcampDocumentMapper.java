package com.pragma.ms_reportes.infrastructure.out.mongodb.mapper;

import com.pragma.ms_reportes.domain.model.Bootcamp;
import com.pragma.ms_reportes.infrastructure.out.mongodb.document.BootcampDocument;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IBootcampDocumentMapper {

    BootcampDocument toDocument(Bootcamp bootcamp);

    Bootcamp toModel(BootcampDocument bootcampDocument);
}
