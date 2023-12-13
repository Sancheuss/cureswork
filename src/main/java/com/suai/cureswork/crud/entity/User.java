package com.suai.cureswork.crud.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_table")
public class User {
    @Id
    Integer id;
    String name;
    String password;
    boolean isActive;
    String roles;
}