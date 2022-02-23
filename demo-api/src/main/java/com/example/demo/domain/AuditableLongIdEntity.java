package com.example.demo.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditableLongIdEntity extends AuditableEntity {
    @Id
    @GeneratedValue
    private Long id;

}
