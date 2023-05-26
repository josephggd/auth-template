package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationCode extends AbstractEntity implements Comparable<ConfirmationCode>{
    @NonNull
    private LocalDate expiration;
    @NonNull
    private UUID code;
    @ManyToOne
    private EscrituraUser user;

    public boolean isExpired(){
        return this.expiration.isAfter(LocalDate.now());
    }
    @Override
    public int compareTo(ConfirmationCode otherCode) {
        boolean equal = otherCode.expiration.isEqual(this.expiration);
        if (equal) {
            return 0;
        } else if (otherCode.expiration.isAfter(this.expiration)) {
            return -1;
        } else {
            return 1;
        }
    }
}
