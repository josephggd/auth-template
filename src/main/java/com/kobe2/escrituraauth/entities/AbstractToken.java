package com.kobe2.escrituraauth.entities;

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
    protected Long expirationPlusHours;
    protected LocalDateTime expiration;
    protected UUID code = UUID.randomUUID();
    public AbstractToken(Long expirationPlusHours){
        this.expirationPlusHours = expirationPlusHours;
        this.expiration = LocalDateTime.now().plusHours(this.expirationPlusHours);
    }
    public boolean isExpired(){
        return this.expiration.isAfter(LocalDateTime.now());
    }
    public void revoke(){
        this.setExpiration(LocalDateTime.MIN);
    }
}
