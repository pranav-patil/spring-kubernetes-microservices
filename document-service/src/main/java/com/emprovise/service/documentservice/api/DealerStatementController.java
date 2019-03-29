package com.emprovise.service.documentservice.api;

import com.emprovise.service.documentservice.client.FinanceServiceClient;
import com.emprovise.service.documentservice.dto.DocumentDTO;
import com.emprovise.service.documentservice.dto.StatementDetailDTO;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.HystrixCommands;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;

@EnableEurekaClient
@RestController
@RequestMapping("/cloud/services/statement")
public class DealerStatementController {

    @Autowired
    private FinanceServiceClient financeServiceClient;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/id/{documentId}")
    public void getDealerStatement(@PathVariable String documentId, HttpServletResponse response) throws Exception {

        String filename = "Statement.pdf";

        try {
            DocumentDTO documentDTO = financeServiceClient.getObject("1234567", documentId);
            byte[] binaryDocument = documentDTO.getDocument();
            response.setContentType("application/pdf");
            InputStream inputStream = new ByteArrayInputStream(binaryDocument);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.getOutputStream().flush();

        }catch (Exception ex) {
            PrintWriter writer = response.getWriter();
            writer.write(ex.getMessage());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".txt");
            writer.flush();
        }
    }

    @GetMapping("/summary/bpId/{payerId}")
    public Flux<StatementDetailDTO> getDealerStatementSummary(@PathVariable String payerId) {
        Flux<StatementDetailDTO> stockDetailFlux = webClientBuilder.build()
                                                            .get().uri("http://data-service/statements/payer/{payerId}", payerId)
                                                            .retrieve().bodyToFlux(StatementDetailDTO.class);

        return HystrixCommands.from(stockDetailFlux)
                                .fallback(Flux.just(new StatementDetailDTO()))
                                .commandName("getStockSummary")
                                .toFlux();
    }

    @GetMapping("/info")
    public String info() {
        return financeServiceClient.info();
    }
}
