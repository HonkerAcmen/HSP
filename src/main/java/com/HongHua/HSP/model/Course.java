package com.HongHua.HSP.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseID;

    private String courseName;
    private String courseDesc;
    @Column(name = "course_img", columnDefinition = "BLOB")
    private String courseImg;
    private String owner;
    private Integer courseImgSize;
    private LocalDateTime lastEditTime;
    private LocalDateTime createTime;
}
