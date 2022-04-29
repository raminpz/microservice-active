package com.nnttdata.microserviceactive.models.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "credits")
public class Credit {

    @Id
    private String id;
    private Client client;
    private Double fullGrantedAmount;
    private Double availableAmount;
    private String creditCardNumber = UUID.randomUUID().toString();
    private Date issueDate;
    private Date dueDate;
}
