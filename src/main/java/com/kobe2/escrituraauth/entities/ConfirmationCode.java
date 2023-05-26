package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ConfirmationCode extends AbstractEntity implements Comparable<ConfirmationCode>{
    private static Long expirationPlusDays = 1L;
    private LocalDate expiration = LocalDate.now().plusDays(expirationPlusDays);
    private UUID code = UUID.randomUUID();
    @ManyToOne
    @NonNull
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
