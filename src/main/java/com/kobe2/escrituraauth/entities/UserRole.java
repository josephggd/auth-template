package com.kobe2.escrituraauth.entities;

import com.kobe2.escrituraauth.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UserRole extends AbstractEntity{
    @NonNull
    private Roles role;
    @ManyToMany
    private Set<EscrituraUser> users;
}
