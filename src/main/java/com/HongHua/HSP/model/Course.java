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
    private Integer courseImgSize;
    private Integer courseTimeSize;
    private LocalDateTime lastEditTime;
    private LocalDateTime createTime;
}