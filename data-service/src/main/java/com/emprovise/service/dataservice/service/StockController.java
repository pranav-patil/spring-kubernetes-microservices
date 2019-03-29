package com.emprovise.service.dataservice.service;

import com.emprovise.service.dataservice.dto.StatementDetail;
import com.emprovise.service.dataservice.resource.DealerStatementResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private DealerStatementResource dealerStatementResource;

    @GetMapping("/{id}")
    private Mono<StatementDetail> getStockById(@PathVariable String id) {
        return dealerStatementResource.findById(id);
    }

//    @GetMapping("/{name}")
//    private Flux<StatementDetail> getStockByName(@PathVariable String name) {
//        return dealerStatementResource.findByDocumentId(name);
//    }

    @GetMapping
    private Flux<StatementDetail> getAllStocks() {
        return dealerStatementResource.all();
    }

    @PostMapping("/add")
    private Mono<StatementDetail> addStock(@RequestBody StatementDetail stock) {
        return dealerStatementResource.add(stock);
    }
}
