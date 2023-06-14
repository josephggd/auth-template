package com.kobe2.escrituraauth.entities;

import com.kobe2.escrituraauth.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Table(name = "escritura_user")
public class EscrituraUser extends AbstractEntity{
    @NonNull
    private String username;
    @NonNull
    private String password;
    @ManyToMany(mappedBy = "users")
    private Set<UserRole> roles;
    public boolean isConfirmed() {
        return this.roles.stream().anyMatch(p->p.getRole().equals(Roles.USER));
    }
}
