package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class AbstractToken extends AbstractEntity {
    @Column(name = "expiration_plus_hours")
    protected Long expirationPlusHours;
    @Column(name = "expiration")
    protected LocalDateTime expiration;
    @Column(name = "code")
    protected UUID code = UUID.randomUUID();
    public AbstractToken(Long expirationPlusHours){
        this.expirationPlusHours = expirationPlusHours;
        this.expiration = LocalDateTime.now().plusHours(this.expirationPlusHours);
    }
    public boolean isExpired(){
        return LocalDateTime.now().isAfter(this.expiration);
    }
    public void revoke(){
        this.setExpiration(LocalDateTime.MIN);
    }
}
