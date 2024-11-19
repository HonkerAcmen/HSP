package com.HongHua.HSP.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserValidateDTO {
    private Long userID;
    private String email;
    private String password;
    private LocalDateTime createTime;
    private LocalDateTime lastEditTime;
}
