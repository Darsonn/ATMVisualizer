package pl.darsonn.api.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Transaction {
    private Integer transactionId;
    private Account account;
    private Card card;
    private Date transactionDate;
    private String transactionType;
    private BigDecimal amount;
}
