package com.HongHua.HSP.mapper;

import com.HongHua.HSP.model.Course;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseMapper {
    @Insert("INSERT INTO courses( COURSE_NAME, course_Desc, course_Time_Size, course_Img, course_Img_Size, last_Edit_Time, create_Time)"+
        "VALUES ( #{courseName}, #{courseDesc}, #{courseTimeSize}, #{courseImg}, #{courseImgSize}, #{lastEditTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "courseID")
    void insertCourse(Course course);

    @Select("SELECT * FROM courses WHERE courseID = #{courseID}")
    Course findByCourseID(Long courseID);
}
