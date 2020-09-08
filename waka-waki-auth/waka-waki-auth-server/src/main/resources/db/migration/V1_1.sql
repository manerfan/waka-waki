/******************************************/
/* 用户信息
/* waki_auth.users
/******************************************/
CREATE TABLE IF NOT EXISTS `users`
(
    `id`           BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT 'id',
    `uid`          VARCHAR(32) NOT NULL COMMENT '唯一标识',
    `nickname`     VARCHAR(64) COMMENT '昵称',
    `avatar`       VARCHAR(512) COMMENT '头像',
    `gmt_create`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_uid` (`uid`)
);

/******************************************/
/* 用户认证信息
/* waki_auth.user_auth
/******************************************/
CREATE TABLE IF NOT EXISTS `user_auth`
(
    `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `uid`           VARCHAR(32)  NOT NULL COMMENT '唯一标识',
    `identity_type` VARCHAR(16)  NOT NULL COMMENT '认证类型(LOGIN_ID,WECHAT,GITHUB,...)',
    `identifier`    VARCHAR(128) NOT NULL COMMENT '认证标识(登录名、微信OPENID、等)',
    `credential`    VARCHAR(128) COMMENT '登录密码或第三方token等',
    `gmt_create`    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_identifier` (`identity_type`, `identifier`)
);

/******************************************/
/* 角色信息
/* waki_auth.user_role
/******************************************/
CREATE TABLE IF NOT EXISTS `user_role`
(
    `id`           BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT 'id',
    `code`         VARCHAR(32) NOT NULL COMMENT '角色码',
    `name`         VARCHAR(32) NOT NULL COMMENT '角色名',
    `gmt_create`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
);

CREATE TABLE IF NOT EXISTS `user_role_mapper`
(
    `id`           BIGINT(20)  NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_uid`     VARCHAR(32) NOT NULL COMMENT '用户唯一标识',
    `role_code`    VARCHAR(32) NOT NULL COMMENT '角色码',
    `gmt_create`   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`user_uid`, `role_code`)
);

INSERT INTO `users` (uid, nickname)
VALUES ('101f693f91301e264ce1001f', '超级管理员');

INSERT INTO `user_auth` (uid, identity_type, identifier, credential)
values ('101f693f91301e264ce1001f', 'LOGIN_ID', 'admin', '$2a$10$BOTS1pvWP7kChYZSZI4AGOkd.RZ54dxpDUlkVshrJC8Zn.4f8cPOO');

INSERT INTO `user_role` (code, name)
VALUES ('admin', '超级管理员'),
       ('user', '普通用户');

INSERT INTO `user_role_mapper` (user_uid, role_code)
VALUES ('101f693f91301e264ce1001f', 'admin');
