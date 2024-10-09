CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键，自增 ID',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '电子邮箱地址',
    password VARCHAR(255) NOT NULL COMMENT '用户密码',
    user_name VARCHAR(255) NOT NULL COMMENT '学生姓名',
    gender TINYINT NOT NULL COMMENT '性别，0 为女，1 为男',
    profile_img VARCHAR(255) NOT NULL COMMENT '头像',
    user_desc TEXT NOT NULL COMMENT '学生描述',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    last_edit_time TIMESTAMP NOT NULL COMMENT '最后修改时间'
);