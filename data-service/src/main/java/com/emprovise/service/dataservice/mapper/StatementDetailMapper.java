package com.emprovise.service.dataservice.mapper;


import com.emprovise.service.dataservice.dto.StatementDetail;
import com.emprovise.service.dataservice.model.Statement;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatementDetailMapper {

    StatementDetail mapToStockDetail(Statement statement);

    @InheritInverseConfiguration
    Statement mapToStock(StatementDetail userBean);
}
