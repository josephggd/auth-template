package com.kobe2.escrituraauth.entities;

import com.kobe2.escrituraauth.enums.Roles;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends AbstractEntity{
    private Roles role;
    @ManyToMany
    private Set<EscrituraUser> users;
}
