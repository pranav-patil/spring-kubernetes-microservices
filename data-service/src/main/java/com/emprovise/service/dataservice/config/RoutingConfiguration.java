package com.emprovise.service.dataservice.config;

import com.emprovise.service.dataservice.dto.StatementDetail;
import com.emprovise.service.dataservice.resource.UserStatementResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RoutingConfiguration {

    @Bean
    public RouterFunction<?> routes(UserStatementResource userStatementResource) {

        return nest(path("/statements"),
                route(GET("/document/{documentId}"),
                    request -> ok().body(userStatementResource.findByDocumentId(request.pathVariable("documentId")), StatementDetail.class))
                    .andRoute(GET("/user/{userId}"),
                            request -> ok().body(userStatementResource.findByUserId(request.pathVariable("userId")), StatementDetail.class))
                    .andRoute(POST("/markread/{documentId}"),
                            request -> ok().body(userStatementResource.updateRead(request.pathVariable("documentId"), true), StatementDetail.class))
                    .andRoute(GET("/all"),
                            request -> ok().body(userStatementResource.all(), StatementDetail.class))
        );
    }
}
