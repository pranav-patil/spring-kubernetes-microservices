package com.emprovise.service.dataservice.repository;

import com.emprovise.service.dataservice.model.Statement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StatementRepository extends ReactiveMongoRepository<Statement, String> {

    Mono<Statement> findByDocumentId(final String documentId);
    Flux<Statement> findByPayerId(final String payerId);
}
