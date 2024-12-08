package com.HongHua.HSP.mapper;

import com.HongHua.HSP.model.Course;
import com.HongHua.HSP.model.CourseDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Insert("INSERT INTO courses( COURSE_NAME, course_Desc, course_Img, OWNER, course_Img_Size, last_Edit_Time, create_Time)"+
        "VALUES ( #{courseName}, #{courseDesc}, #{courseImg}, #{owner}, #{courseImgSize}, #{lastEditTime}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "courseID")
    void insertCourse(Course course);

    @Select("SELECT * FROM courses WHERE courseID = #{courseID}")
    Course findByCourseID(Long courseID);

    @Select("SELECT * FROM courses WHERE courseID = #{courseID}")
    CourseDTO findCourseByIDDTO(Long courseID);

    @Update("UPDATE courses SET OWNER = #{owner} WHERE courseID = #{courseID}")
    void updateCourseOwner(Long courseID, String owner);

    @Select("SELECT * FROM courses")
    List<CourseDTO> allCourseData();
}
