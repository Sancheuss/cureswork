package com.suai.cureswork.crud.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Map;

@Entity
@Data
@Table(name = "groups")
public class Groups {
    @Id
    @Column(name = "group_name")
    String group;
}
