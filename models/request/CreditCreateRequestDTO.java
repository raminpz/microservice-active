package com.nnttdata.microserviceactive.models.request;

import lombok.Data;

import java.util.Date;

@Data
public class CreditCreateRequestDTO {
    private String clientId;
    private Double fullGrantedAmount;
    private Date issueDate;
    private Date dueDate;
}
