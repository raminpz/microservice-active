package com.nnttdata.microserviceactive.models.response;

import lombok.Data;

@Data
public class CreditFindBalancesResponseDTO {
    private String id;
    private Double fullGrantedAmount;
    private Double availableAmount;
}
