package com.HongHua.HSP.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {
    private Long userID;
    private String userName;
    private String email;
    private Integer gender;
    private String profileImg;
    private String userDesc;
    private LocalDateTime lastEditTime;

}
