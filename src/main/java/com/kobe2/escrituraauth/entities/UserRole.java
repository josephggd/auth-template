package com.kobe2.escrituraauth.entities;

import com.kobe2.escrituraauth.enums.Roles;
import jakarta.persistence.*;
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
    @JoinTable(
            name = "user_role_users",
            joinColumns = { @JoinColumn(name = "users_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_role_id") }
    )
    private Set<EscrituraUser> users;

    @Override
    public String getAuthority() {
        return role.toString();
    }
}
