package com.kobe2.escrituraauth.entities;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class AbstractEntity {
    @Id
    private UUID id;
    @CreatedDate
    private LocalDate created;

    @PrePersist
    public void save(){
        if (this.id==null) {
            this.id = UUID.randomUUID();
        }
        if (this.created==null) {
            this.created = LocalDate.now();
        }
    }
}
