package com.example.demo.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "TODO")
@Getter
@Setter
@ToString
public class TodoEntity extends AuditableStringIdEntity {
    @Column(length = 128)
    private String label;
    @Lob
    private String description;
}
