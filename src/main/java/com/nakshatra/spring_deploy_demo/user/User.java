package com.nakshatra.spring_deploy_demo.user;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(unique = true)
    private String username;
    
    @JsonIgnore
    @Column(unique = true)
    private String email;
    
    
    @JsonIgnore
    private String password;
}