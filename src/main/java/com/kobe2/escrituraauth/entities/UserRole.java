package com.kobe2.escrituraauth.entities;

import com.kobe2.escrituraauth.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UserRole extends AbstractEntity implements GrantedAuthority {
    @NonNull
    private Roles role;
    @ManyToMany
    private Set<EscrituraUser> users;

    @Override
    public String getAuthority() {
        return role.toString();
    }
}
