package com.HongHua.HSP.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {
    private String email;
    private String userName;
    private Integer gender;
    private String profileImg;
    private String userDesc;
}
