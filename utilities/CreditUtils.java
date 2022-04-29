package com.nnttdata.microserviceactive.utilities;

import com.nnttdata.microserviceactive.models.dto.Credit;
import com.nnttdata.microserviceactive.models.request.CreditCreateRequestDTO;
import com.nnttdata.microserviceactive.models.request.CreditUpdateRequestDTO;
import com.nnttdata.microserviceactive.models.response.CreditFindBalancesResponseDTO;

public interface CreditUtils {
    Credit creditCreateRequestDTOToCredit(CreditCreateRequestDTO creditDTO);
    Credit creditUpdateRequestDTOToCredit(CreditUpdateRequestDTO creditDTO);
    Credit creditFindBalancesResponseDTOToCredit(CreditFindBalancesResponseDTO creditDTO);
    CreditUpdateRequestDTO creditToCreditUpdateCreateRequestDTO(Credit credit);
    CreditCreateRequestDTO creditToCreditCreateRequestDTO(Credit credit);
    CreditFindBalancesResponseDTO creditToCreditFindBalancesResponseDTO(Credit credit);
    Credit fillCreditWithCreditUpdateRequestDTO(Credit credit, CreditUpdateRequestDTO creditDTO);
}
