package pl.darsonn.api.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class Card {
    private Integer cardId;
    private Account account;
    private String cardNumber;
    private Integer pin;
    private Date expirationDate;
}
