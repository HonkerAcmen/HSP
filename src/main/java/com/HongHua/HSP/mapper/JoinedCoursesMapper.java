package com.HongHua.HSP.mapper;

import com.HongHua.HSP.model.CourseDTO;
import com.HongHua.HSP.model.JoinedCourses;
import com.HongHua.HSP.model.UserDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JoinedCoursesMapper {
    @Insert("INSERT INTO joined_courses (userID, courseID, create_Time, last_Edit_Time)" +
            "VALUES (#{userID},#{courseID},#{createTime},#{lastEditTime})")
    @Options(useGeneratedKeys = true, keyProperty = "joinCourseId")
    void insertJoinCourse(JoinedCourses joinedCourses);

    @Select("SELECT c.courseID, c.COURSE_NAME, c.course_Desc, c.course_Img, c.OWNER, c.course_Img_Size FROM courses c " +
            "JOIN joined_courses jc ON c.courseID = jc.courseID " +
            "WHERE jc.userID = #{userID}")
    List<CourseDTO> returnUserOfCourse(Long userID);
}
