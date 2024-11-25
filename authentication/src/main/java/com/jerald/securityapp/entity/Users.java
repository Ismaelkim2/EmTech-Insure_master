package com.jerald.securityapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table (name = "InsureMasterUsers")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;
//    @Column(nullable = false)
    private String password;
}