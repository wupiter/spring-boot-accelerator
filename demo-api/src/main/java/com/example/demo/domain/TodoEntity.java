package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
