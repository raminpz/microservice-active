package com.nnttdata.microserviceactive.controller;

import com.nnttdata.microserviceactive.models.dto.Credit;
import com.nnttdata.microserviceactive.models.request.CreditCreateRequestDTO;
import com.nnttdata.microserviceactive.models.request.CreditUpdateRequestDTO;
import com.nnttdata.microserviceactive.models.response.ClientClientServiceResponseDTO;
import com.nnttdata.microserviceactive.service.CreditService;
import com.nnttdata.microserviceactive.utilities.errorhandling.BusinessLogicException;
import com.nnttdata.microserviceactive.utilities.errorhandling.ElementBlockedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/active")
public class CreditController {

    @Autowired
    CreditService creditService;

    @GetMapping("/credits")
    public Flux<Credit> findAllCredits(){
        log.info("Get operation in /credits");
        return creditService.findAll();
    }

    @GetMapping("/credits/{id}")
    public Mono<ResponseEntity<Credit>> findCreditById(@PathVariable("id") String id) {
        log.info("Get operation in /credits/{}", id);
        return creditService.findById(id)
                .flatMap(retrievedCredit -> Mono.just(ResponseEntity.ok(retrievedCredit)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/credits")
    public Mono<ResponseEntity<Credit>> createCredit(@RequestBody CreditCreateRequestDTO creditDTO) {
        log.info("Post operation in /credits");
        return creditService.create(creditDTO)
                .flatMap(createdCredit -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdCredit)))
                .onErrorResume(ElementBlockedException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(BusinessLogicException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(null)));
    }

    @PutMapping("/credits")
    public Mono<ResponseEntity<Credit>> updateCredit(@RequestBody CreditUpdateRequestDTO creditDTO) {
        log.info("Put operation in /credits");
        return creditService.update(creditDTO)
                .flatMap(createdCredit -> Mono.just(ResponseEntity.ok(createdCredit)))
                .onErrorResume(ElementBlockedException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(BusinessLogicException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/credits/{id}")
    public Mono<ResponseEntity<Credit>> deleteCredit(@PathVariable("id") String id) {
        return creditService.removeById(id)
                .flatMap(removedCredit -> Mono.just(ResponseEntity.ok(removedCredit)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    //endregion

    //region Additional Repository Endpoints
    @GetMapping("customers/{id}/credits")
    public Flux<Credit> findCreditsByCustomerId(@PathVariable("id") String id) {
        return creditService.findByClientId(id);
    }

    @GetMapping("customers-service/{id}")
    public Mono<ResponseEntity<ClientClientServiceResponseDTO>> findByIdCustomerService(@PathVariable("id") String id) {
        return creditService.findByIdCustomerService(id)
                .flatMap(retrievedCustomer -> Mono.just(ResponseEntity.ok(retrievedCustomer)));
    }


}
