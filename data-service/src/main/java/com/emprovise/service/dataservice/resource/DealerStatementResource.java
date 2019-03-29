package com.emprovise.service.dataservice.resource;

import com.emprovise.service.dataservice.dto.StatementDetail;
import com.emprovise.service.dataservice.mapper.StatementDetailMapper;
import com.emprovise.service.dataservice.model.Statement;
import com.emprovise.service.dataservice.repository.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DealerStatementResource {

    @Autowired
    private StatementDetailMapper statementDetailMapper;
    private final StatementRepository statementRepository;

    DealerStatementResource(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    public Mono<StatementDetail> findById(String id) {
        return this.statementRepository.findById(id)
                                   .map(statement -> statementDetailMapper.mapToStockDetail(statement));
    }

    public Mono<StatementDetail> findByDocumentId(String name) {
        return this.statementRepository.findByDocumentId(name)
                                   .map(statement -> statementDetailMapper.mapToStockDetail(statement));
    }

    public Flux<StatementDetail> findByPayerId(String name) {
        return this.statementRepository.findByPayerId(name)
                                   .map(statement -> statementDetailMapper.mapToStockDetail(statement));
    }

    public Flux<StatementDetail> all() {
        Flux<Statement> stocks = this.statementRepository.findAll();
        return stocks.map(statementDetailMapper::mapToStockDetail);
    }

    public Mono<StatementDetail> add(StatementDetail statementDetail) {
        Statement statement = statementDetailMapper.mapToStock(statementDetail);
        return this.statementRepository.insert(statement)
                                   .map(stk -> statementDetailMapper.mapToStockDetail(stk));
    }
}
