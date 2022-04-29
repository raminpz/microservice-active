package com.nnttdata.microserviceactive.models.response;

import lombok.Data;

@Data
public class ClientClientServiceResponseDTO {
    private String id;
    private ClientTypeCustomerServiceResponseDTO clientType;;
    private String status;
    private PersonDetailsCustomerServiceResponseDTO personDetails;
}
