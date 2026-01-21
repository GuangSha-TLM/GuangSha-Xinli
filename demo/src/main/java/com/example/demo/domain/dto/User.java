package com.example.demo.domain.dto;

import com.example.demo.domain.entity.BaseEntity;
import lombok.Data;

@Data
public class User extends BaseEntity {
    private String username;
    private String password;
    private Integer role;
}
