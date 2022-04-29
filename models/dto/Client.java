package com.nnttdata.microserviceactive.models.dto;

import lombok.Data;

@Data
public class Client {
    private String id;
    private ClientType clientType;
    private String status;
}
