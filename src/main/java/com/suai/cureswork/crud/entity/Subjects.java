package com.suai.cureswork.crud.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "subjects")
public class Subjects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String subject;
    @Column(name = "group_name")
    String group;
    Integer numberRows;
    Integer numberColumns;
}
