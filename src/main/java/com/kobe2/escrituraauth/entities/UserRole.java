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
    @Enumerated(EnumType.ORDINAL)
    private Roles role;
    @ManyToOne
//    @JoinTable(
//            name = "user_role_users",
//            joinColumns = { @JoinColumn(name = "users_id") },
//            inverseJoinColumns = { @JoinColumn(name = "user_role_id") }
//    )
    private final EscrituraUser user;

    @Override
    public String getAuthority() {
        return role.toString();
    }
}
