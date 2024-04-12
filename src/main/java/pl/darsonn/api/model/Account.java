package pl.darsonn.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    private Integer accountId;
    private Client client;
    private String accountNumber;
    private Float accountBalance;
}
