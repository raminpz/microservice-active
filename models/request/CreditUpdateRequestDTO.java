package com.nnttdata.microserviceactive.models.request;

import lombok.Data;

import java.util.Date;

@Data
public class CreditUpdateRequestDTO {
    private String id;
    private String status;
    private Double fullGrantedAmount;
    private Double availableAmount;
    private Date dueDate;
}
