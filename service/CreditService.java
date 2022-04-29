package com.nnttdata.microserviceactive.service;

import com.nnttdata.microserviceactive.models.dto.Credit;
import com.nnttdata.microserviceactive.models.request.CreditCreateRequestDTO;
import com.nnttdata.microserviceactive.models.request.CreditUpdateRequestDTO;
import com.nnttdata.microserviceactive.models.response.ClientClientServiceResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Mono<Credit> create(CreditCreateRequestDTO creditDTO);
    Mono<Credit> findById(String id);
    Flux<Credit> findAll();
    Mono<Credit> update(CreditUpdateRequestDTO creditDTO);
    Mono<Credit> removeById(String id);
    Mono<ClientClientServiceResponseDTO> findByIdCustomerService(String id);
    Flux<Credit> findByClientId(String id);
}
