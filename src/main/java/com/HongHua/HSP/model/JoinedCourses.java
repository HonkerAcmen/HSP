package com.HongHua.HSP.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "joined_courses")
public class JoinedCourses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinCourseId;

    private Long courseID;
    private Long userID;
    private LocalDateTime lastEditTime;
    private LocalDateTime createTime;
}
