package com.emprovise.service.dataservice;

import com.emprovise.service.dataservice.dto.StatementDetail;
import com.emprovise.service.dataservice.resource.DealerStatementResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@EnableReactiveMongoRepositories
@SpringBootApplication
@EnableDiscoveryClient
public class DataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataServiceApplication.class, args);
    }

    @Bean
    RouterFunction<?> routes(DealerStatementResource dealerStatementResource) {

        return nest(path("/statements"),
                route(RequestPredicates.GET("/document/{id}"),
                        request -> ok().body(dealerStatementResource.findByDocumentId(request.pathVariable("id")), StatementDetail.class))
                .andRoute(GET("/payer/{payerId}"),
                        request -> ok().body(dealerStatementResource.findByPayerId(request.pathVariable("payerId")), StatementDetail.class))
                .andRoute(GET("/all"),
                        request -> ok().body(dealerStatementResource.all(), StatementDetail.class))
        );
    }
}
