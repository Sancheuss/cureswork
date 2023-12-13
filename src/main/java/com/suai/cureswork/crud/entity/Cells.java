package com.suai.cureswork.crud.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cells")
public class Cells {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer subjectId;
    @Column(name = "`row`")
    Integer row;
    @Column(name = "`column`")
    Integer column;
    String value;
}
