package com.HongHua.HSP.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "user_courses")
public class UserCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCourseID;

    private Long courseID;
    private Long userID;
    private LocalDateTime lastEditTime;
    private LocalDateTime createTime;
}
