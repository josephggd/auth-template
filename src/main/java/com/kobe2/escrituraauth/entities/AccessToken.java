package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AccessToken extends AbstractToken {
    public AccessToken(
            EscrituraUser user
    ){
        super(1L);
        this.user = user;
    }
    public AccessToken() {
        super(1L);
    }
    @OneToOne
    private EscrituraUser user;
}
