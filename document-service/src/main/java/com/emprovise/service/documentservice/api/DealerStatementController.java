package com.emprovise.service.documentservice.api;

import com.emprovise.service.documentservice.client.StorageServiceClient;
import com.emprovise.service.documentservice.dto.DealerStatementDTO;
import com.emprovise.service.documentservice.dto.DocumentDTO;
import com.emprovise.service.documentservice.dto.StatementDetailDTO;
import com.emprovise.service.documentservice.mapper.DealerStatementDTOMapper;
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
import reactor.core.publisher.Mono;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.Duration;

@EnableEurekaClient
@RestController
@RequestMapping("/cloud/services/statement")
public class DealerStatementController {

    @Autowired
    private StorageServiceClient storageServiceClient;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private DealerStatementDTOMapper dealerStatementDTOMapper;

    @GetMapping("/id/{documentId}")
    public void getDealerStatement(@PathVariable String documentId, HttpServletResponse response) throws Exception {

        String objectId = "error";

        try {
            Mono<StatementDetailDTO> detailDTOMono = webClientBuilder.build()
                    .get().uri("http://data-service/statements/document/{documentId}", documentId)
                    .retrieve().bodyToMono(StatementDetailDTO.class);

            StatementDetailDTO statementDetailDTO = detailDTOMono.block(Duration.ofSeconds(2));
            objectId = statementDetailDTO.getDocumentReference();
            DocumentDTO documentDTO = storageServiceClient.getObject("1234567", objectId);
            byte[] binaryDocument = documentDTO.getBinaryFile();
            response.setContentType(getFileContentType(objectId));
            InputStream inputStream = new ByteArrayInputStream(binaryDocument);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=" + objectId);

            webClientBuilder.build().post().uri("http://data-service/statements/markread/{documentId}", documentId)
                     .retrieve().bodyToMono(StatementDetailDTO.class).subscribe();

            response.getOutputStream().flush();

        }catch (Exception ex) {
            ex.printStackTrace();
            PrintWriter writer = response.getWriter();
            writer.write(ex.getMessage());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.setHeader("Content-Disposition", "attachment; filename=" + objectId + ".txt");
            writer.flush();
        }
    }

    public String getFileContentType(final String fileName) {
        final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        return fileTypeMap.getContentType(fileName);
    }

    @GetMapping("/summary/bpId/{payerId}")
    public Flux<DealerStatementDTO> getDealerStatementSummary(@PathVariable String payerId) {
        Flux<StatementDetailDTO> statementDetailFlux = webClientBuilder.build()
                                                            .get().uri("http://data-service/statements/payer/{payerId}", payerId)
                                                            .retrieve().bodyToFlux(StatementDetailDTO.class);

        Flux<DealerStatementDTO> dealerStatementDTOFlux = statementDetailFlux.map(dealerStatementDTOMapper::mapToDealerStatementDTO);

        return HystrixCommands.from(dealerStatementDTOFlux)
                                .fallback(Flux.just(new DealerStatementDTO()))
                                .commandName("getDealerStatementSummary")
                                .toFlux();
    }

    @GetMapping("/info")
    public String info() {
        return storageServiceClient.info();
    }
}
