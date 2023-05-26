package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class AbstractEntity {
    @Id
    private UUID id;
    @CreatedDate
    private LocalDate created;


}
