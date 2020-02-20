package com.driw.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class User {

    @Id
    private Long userId;

    private String name;

    private String dept;

    private BigDecimal account;
}
