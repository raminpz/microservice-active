package com.nnttdata.microserviceactive.utilities;

import com.nnttdata.microserviceactive.models.dto.Client;
import com.nnttdata.microserviceactive.models.response.ClientClientServiceResponseDTO;

public interface ClientUtils {
    ClientUtils customerCustomerServiceDTOToCustomer(ClientClientServiceResponseDTO customerDTO);
    ClientClientServiceResponseDTO customerToCustomerCustomerServiceResponseDTO(Client customer);
}
