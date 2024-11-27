package com.HongHua.HSP.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CourseDTO {
    private Long courseID;
    private String courseName;
    private String courseDesc;
    private String owner;
    private String courseImg;
    private Integer courseImgSize;

}
