package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken extends AbstractToken {
    public ConfirmationToken(
            EscrituraUser user
    ){
        super(24L);
        this.user = user;
    }
    public ConfirmationToken() {
        super(24L);
    }
    @OneToOne
    private EscrituraUser user;
}
