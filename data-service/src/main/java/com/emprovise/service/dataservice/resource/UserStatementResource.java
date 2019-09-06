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
public class UserStatementResource {

    @Autowired
    private StatementDetailMapper statementDetailMapper;
    private final StatementRepository statementRepository;

    UserStatementResource(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    public Mono<StatementDetail> findById(String id) {
        return this.statementRepository.findById(id)
                                   .map(statement -> statementDetailMapper.mapToStatementDetail(statement));
    }

    public Mono<StatementDetail> findByDocumentId(String documentId) {
        return this.statementRepository.findByDocumentId(documentId)
                                   .map(statement -> statementDetailMapper.mapToStatementDetail(statement));
    }

    public Flux<StatementDetail> findByUserId(String name) {
        return this.statementRepository.findByUserId(name)
                                   .map(statement -> statementDetailMapper.mapToStatementDetail(statement));
    }

    public Flux<StatementDetail> all() {
        Flux<Statement> statements = this.statementRepository.findAll();
        return statements.map(statementDetailMapper::mapToStatementDetail);
    }

    public Mono<StatementDetail> add(StatementDetail statementDetail) {
        Statement statement = statementDetailMapper.mapToStatement(statementDetail);
        return this.statementRepository.insert(statement)
                                   .map(stk -> statementDetailMapper.mapToStatementDetail(stk));
    }

    public Mono<StatementDetail> updateRead(String documentId, boolean read) {
        return this.statementRepository.findByDocumentId(documentId).doOnSuccess(statement -> {
                    statement.setRead(read);
                    statementRepository.save(statement).subscribe();
                }).map(statementDetailMapper::mapToStatementDetail);
    }
}
