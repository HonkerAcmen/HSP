package com.HongHua.HSP.mapper;


import com.HongHua.HSP.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void registerUser(User user);

    User findUserByEmail(@Param("email") String email);

    void modifyUserData(User user);

    User returnUserInfoById(@Param("id")Long id);
}
