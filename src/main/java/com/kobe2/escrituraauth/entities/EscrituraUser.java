package com.kobe2.escrituraauth.entities;

import com.kobe2.escrituraauth.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Optional;
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
    @OneToMany
    private Set<AuthenticationCode> codes;
    @ManyToMany
    private Set<UserRole> roles;
    public AuthenticationCode getMostRecentCode() {
        Optional<AuthenticationCode> optional = this.getCodes().stream().sorted().findFirst();
        if (optional.isPresent()){
            return optional.get();
        } else {
            throw new IllegalArgumentException("NO CODE FOUND");
        }
    }
    public boolean isConfirmed() {
        return this.roles.stream().anyMatch(p->p.getRole().equals(Roles.USER));
    }
}
