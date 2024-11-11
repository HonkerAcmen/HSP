package com.HongHua.HSP.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long userID;
    private String email;
    private String password;
    private String userName;
    private Integer gender;
    private String profileImg;
    private String userDesc;
    private LocalDateTime createTime;
    private LocalDateTime lastEditTime;
}