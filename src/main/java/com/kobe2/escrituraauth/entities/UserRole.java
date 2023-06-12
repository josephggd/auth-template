package com.kobe2.escrituraauth.entities;

import com.kobe2.escrituraauth.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "user_role")
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
