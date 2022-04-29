package com.nnttdata.microserviceactive.models.response;

import lombok.Data;

import java.util.Date;

@Data
public class PersonDetailsCustomerServiceResponseDTO {
    private String name;
    private String lastname;
    private String identityNumber;
    private String email;
    private String phoneNumber;
    private Date birthdate;
}
