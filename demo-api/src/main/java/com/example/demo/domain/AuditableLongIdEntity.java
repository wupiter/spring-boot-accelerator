package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class AuditableLongIdEntity extends AuditableEntity {
    @Id
    @GeneratedValue
    private Long id;

}
