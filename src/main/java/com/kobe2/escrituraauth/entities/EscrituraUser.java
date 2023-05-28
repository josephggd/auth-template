package com.kobe2.escrituraauth.entities;

import com.kobe2.escrituraauth.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class EscrituraUser extends AbstractEntity{
    @NonNull
    private String email;
    @NonNull
    private String pw;
    @OneToOne
    private ConfirmationToken cToken;
    @ManyToMany
    private Set<UserRole> roles;
    public boolean isConfirmed() {
        return this.roles.stream().anyMatch(p->p.getRole().equals(Roles.USER));
    }
}
