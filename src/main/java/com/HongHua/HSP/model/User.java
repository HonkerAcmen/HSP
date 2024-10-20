package com.HongHua.HSP.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String userName;
    private Integer gender;
    private String profileImg;
    private String userDesc;
    private LocalDateTime createTime;
    private LocalDateTime lastEditTime;

}