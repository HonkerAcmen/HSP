package com.HongHua.HSP.mapper;


import com.HongHua.HSP.model.User;
import com.HongHua.HSP.model.UserDTO;
import com.HongHua.HSP.model.UserValidateDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO users (email, password, create_time, last_edit_time) VALUES (#{email}, #{password}, #{createTime}, #{lastEditTime})")
    void registerUser(UserValidateDTO user);

    @Select("SELECT * FROM users WHERE email = #{email}")
    User findUserByEmail(@Param("email") String email);

    @Select("SELECT * FROM users WHERE email = #{email}")
    UserDTO findUserByEmailIsUserDTO(@Param("email") String email);

    @Update("UPDATE users SET user_name = #{userName}, gender = #{gender}, profile_img = #{profileImg}, user_desc = #{userDesc}, last_edit_time = #{lastEditTime} WHERE USERID = #{userID}")
    void modifyUserData(UserDTO user);

}
