package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RefreshToken extends AbstractToken {
    public RefreshToken(
            EscrituraUser user
    ){
        super(12L);
        this.user = user;
    }
    public RefreshToken() {
        super(12L);
    }
    @OneToOne
    private EscrituraUser user;
}
