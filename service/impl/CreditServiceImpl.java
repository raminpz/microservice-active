package com.project.activeservice.service.impl;

import com.nnttdata.microserviceactive.config.Constants;
import com.nnttdata.microserviceactive.models.dto.Client;
import com.nnttdata.microserviceactive.models.dto.Credit;
import com.nnttdata.microserviceactive.models.request.CreditCreateRequestDTO;
import com.nnttdata.microserviceactive.models.request.CreditUpdateRequestDTO;
import com.nnttdata.microserviceactive.models.response.ClientClientServiceResponseDTO;
import com.nnttdata.microserviceactive.models.response.CreditFindBalancesResponseDTO;
import com.nnttdata.microserviceactive.repository.CreditRepository;
import com.nnttdata.microserviceactive.service.CreditService;
import com.nnttdata.microserviceactive.utilities.ClientUtils;
import com.nnttdata.microserviceactive.utilities.CreditUtils;

import com.nnttdata.microserviceactive.utilities.errorhandling.BusinessLogicException;
import com.nnttdata.microserviceactive.utilities.errorhandling.ElementBlockedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService {

    @Autowired
    CreditRepository creditRepository;
    WebClient.Builder webClientBuilder;
    @Autowired
    Constants constants;

    CreditUtils creditUtils;

    ClientUtils clientUtils;
    ReactiveCircuitBreaker customersServiceReactiveCircuitBreaker;
    SecureRandom randomInstance = new SecureRandom();

    @Override
    public Mono<Credit> create(CreditCreateRequestDTO creditDTO) {
        log.info("Start of operation to create a credit");

        if (creditDTO.getClientId() == null || !creditDTO.getClientId().isBlank()) {
            Mono<Credit> createdCredit = findByIdCustomerService(creditDTO.getClientId())
                    .flatMap(retrievedCustomer -> {
                        return creditToCreateValidation(creditDTO, retrievedCustomer);
                    })
                    .flatMap(validatedCustomer -> {
                        Credit creditToCreate = creditUtils.creditCreateRequestDTOToCredit(creditDTO);
                        Client client = (Client) clientUtils.customerCustomerServiceDTOToCustomer(validatedCustomer);

                        creditToCreate.setClient(client);
                        creditToCreate.setAvailableAmount(creditToCreate.getFullGrantedAmount());

                        return creditRepository.insert(creditToCreate);
                    })
                    .switchIfEmpty(Mono.error(new NoSuchElementException("Customer does not exist")));
            return createdCredit;
        } else {
            return Mono.error(new IllegalArgumentException("Credit does not contain customer id"));
        }
    }

    @Override
    public Mono<Credit> findById(String id) {
        Mono<Credit> retrievedCredit = creditRepository.findById(id);
        return retrievedCredit;
    }

    @Override
    public Flux<Credit>  findAll() {
        Flux<Credit> retrievedCredit = creditRepository.findAll();
        return retrievedCredit;
    }

    @Override
    public Mono<Credit> update(CreditUpdateRequestDTO creditDTO) {
        Mono<Credit> updatedCredit = findById(creditDTO.getId())
                .flatMap(retrievedCredit -> {
                    return creditToUpdateValidation(creditDTO, retrievedCredit);
                })
                .flatMap(validatedCredit -> {
                    Credit creditToUpdate = creditUtils.fillCreditWithCreditUpdateRequestDTO(validatedCredit, creditDTO);
                    Mono<Credit> nestedUpdatedCredit = creditRepository.save(creditToUpdate);
                    return nestedUpdatedCredit;
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Credit does not exist")));

        return updatedCredit;
    }

    @Override
    public Mono<Credit> removeById(String id) {
        Mono<Credit> removedAccount = findById(id)
                .flatMap(retrievedAccount -> creditRepository.deleteById(retrievedAccount.getId()).thenReturn(retrievedAccount));
        return removedAccount;
    }

    @Override
    public Mono<ClientClientServiceResponseDTO> findByIdCustomerService(String id) {
        return null;
    }


    @Override
    public Flux<Credit> findByClientId(String id) {
        Flux<Credit> retrievedAccount = creditRepository.findCreditsByClientId(id);

        return retrievedAccount;
    }

    public Flux<CreditFindBalancesResponseDTO> findBalancesByCustomerId(String id) {
        Flux<CreditFindBalancesResponseDTO> retrievedBalances = findByClientId(id)
                .map(creditUtils::creditToCreditFindBalancesResponseDTO);
        return retrievedBalances;
    }

    //region Private Helper Functions
    private Mono<ClientClientServiceResponseDTO> creditToCreateValidation(CreditCreateRequestDTO creditForCreate, ClientClientServiceResponseDTO customerFromMicroservice) {
        if (customerFromMicroservice.getStatus().contentEquals(constants.getStatusBlocked()))
        {
            return Mono.error(new ElementBlockedException("The customer have blocked status"));
        }

        if (customerFromMicroservice.getClientType().getGroup().contentEquals(constants.getCustomerPersonalGroup()))
        {
            return findByClientId(customerFromMicroservice.getId())
                    .hasElements()
                    .flatMap(haveAnAccount -> {
                        if (Boolean.TRUE.equals(haveAnAccount)) {
                            return Mono.error(new BusinessLogicException("Customer already have one credit"));
                        }
                        else {
                            return Mono.just(customerFromMicroservice);
                        }
                    });
        } else {
            return Mono.just(customerFromMicroservice);
        }
    }

    private Mono<Credit> creditToUpdateValidation(CreditUpdateRequestDTO creditForUpdate, Credit creditInDatabase) {
        if (creditInDatabase.getClient().getStatus().contentEquals(constants.getStatusBlocked())) {
            return Mono.error(new ElementBlockedException("The customer have blocked status"));
        }
        if (creditForUpdate.getAvailableAmount() > creditInDatabase.getFullGrantedAmount()) {
            return Mono.error(new BusinessLogicException("Available amount greater than full granted amount"));
        }
        return Mono.just(creditInDatabase);
    }
}