package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EscrituraUser extends AbstractEntity{
    @NonNull
    private String email;
    @NonNull
    private String pw;
    private boolean enabled = false;
    @OneToMany
    private Set<ConfirmationCode> code;
}
