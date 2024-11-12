package com.HongHua.HSP.mapper;

import com.HongHua.HSP.model.Course;
import com.HongHua.HSP.model.CourseDTO;
import com.HongHua.HSP.model.UserCourse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface UserCourseMapper {
    @Insert("INSERT INTO user_courses (userID, courseID, create_Time, last_Edit_Time)" +
            "VALUES (#{userID},#{courseID},#{createTime},#{lastEditTime})")
    @Options(useGeneratedKeys = true, keyProperty = "userCourseID")
    void insertUserCourse(UserCourse userCourse);

    @Select("SELECT c.COURSE_NAME, c.course_Desc, c.course_Img, c.course_Img_Size FROM courses c " +
            "JOIN user_courses uc ON c.courseID = uc.courseID " +
            "WHERE uc.userID = #{userID}")
    List<CourseDTO> findCoursesByUserID(Long userID);
}
