package com.emprovise.service.documentservice.mapper;

import com.emprovise.service.documentservice.dto.DealerStatementDTO;
import com.emprovise.service.documentservice.dto.StatementDetailDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DealerStatementDTOMapper {

    @Mappings({
            @Mapping(target="period", source = "statement.date",
                    dateFormat = "MMM-yyyy")})
    DealerStatementDTO mapToDealerStatementDTO(StatementDetailDTO statement);

    @InheritInverseConfiguration
    @Mappings({
            @Mapping(target="date", source = "statementDTO.period",
                    dateFormat = "MMM-yyyy")})
    StatementDetailDTO mapToStatementDetailDTO(DealerStatementDTO statementDTO);
}
