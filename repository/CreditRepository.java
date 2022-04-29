package com.nnttdata.microserviceactive.repository;

import com.nnttdata.microserviceactive.models.dto.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
    Flux<Credit> findCreditsByClientId(String id);
}
