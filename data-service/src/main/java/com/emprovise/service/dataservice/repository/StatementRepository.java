package com.emprovise.service.dataservice.repository;

import com.emprovise.service.dataservice.model.Statement;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StatementRepository extends ReactiveCassandraRepository<Statement, String> {

    Mono<Statement> findByDocumentId(final String documentId);
    Flux<Statement> findByUserId(final String userId);
}
