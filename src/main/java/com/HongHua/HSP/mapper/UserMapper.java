package com.HongHua.HSP.mapper;


import com.HongHua.HSP.model.User;
import com.HongHua.HSP.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void registerUser(User user);

    User findUserByEmail(@Param("email") String email);
    UserDTO findUserByEmailUseDTO(@Param("email") String email);
    void modifyUserData(User user);

    User returnUserInfoById(@Param("id")Long id);
    Long returnUserIDByEmail(@Param("email") String email);
}
