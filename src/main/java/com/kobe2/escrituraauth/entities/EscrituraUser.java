package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    private boolean enabled = false;
    @OneToMany
    private Set<ConfirmationCode> codes;
    @ManyToMany
    private Set<UserRole> roles;
}
