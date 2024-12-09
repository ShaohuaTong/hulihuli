# 数据库初始化

-- 创建库
create database if not exists hulihuli;

-- 切换库
use hulihuli;

-- ----------------------------
-- t_user 的表结构
-- ----------------------------
CREATE TABLE IF NOT EXISTS t_user (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
                          `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
                          `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
                          `salt` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '盐值',
                          `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                          `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

-- ----------------------------
-- t_user_info 的表结构
-- ----------------------------
CREATE TABLE IF NOT EXISTS t_user_info (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `userId` bigint DEFAULT NULL COMMENT '用户id',
                               `nick` varchar(100) DEFAULT NULL COMMENT '昵称',
                               `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
                               `sign` text COMMENT '签名',
                               `gender` varchar(2) DEFAULT NULL COMMENT '性别：0男 1女 2未知',
                               `birth` varchar(20) DEFAULT NULL COMMENT '生日',
                               `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                               `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户基本信息表';

-- ----------------------------
-- t_following_group 的表结构
-- ----------------------------
CREATE TABLE IF NOT EXISTS t_following_group (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                     `userId` bigint DEFAULT NULL COMMENT '用户id',
                                     `name` varchar(50) DEFAULT NULL COMMENT '关注分组名称',
                                     `type` varchar(5) DEFAULT NULL COMMENT '关注分组类型：0特别关注  1悄悄关注 2默认分组  3用户自定义分组',
                                     `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                                     `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关注分组表';

-- ----------------------------
-- t_user_following 的表结构
-- ----------------------------
CREATE TABLE IF NOT EXISTS t_user_following (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                    `userId` bigint DEFAULT NULL COMMENT '用户id',
                                    `followingId` int DEFAULT NULL COMMENT '关注用户id',
                                    `groupId` int DEFAULT NULL COMMENT '关注分组id',
                                    `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户关注表';

-- ----------------------------
-- t_user_moments 的表结构
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_user_moments` (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                  `userId` bigint DEFAULT NULL COMMENT '用户id',
                                  `type` varchar(5) DEFAULT NULL COMMENT '动态类型：0视频 1直播 2专栏动态',
                                  `contentId` bigint DEFAULT NULL COMMENT '内容详情id',
                                  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                                  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户动态表';

-- ----------------------------
-- Table structure for t_auth_element_operation
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_auth_element_operation` (
                                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                            `elementName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '页面元素名称',
                                            `elementCode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '页面元素唯一编码',
                                            `operationType` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作类型：0可点击  1可见',
                                            `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                                            `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
                                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='权限控制--页面元素操作表';

-- ----------------------------
-- Table structure for t_auth_menu
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_auth_menu` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单项目名称',
                               `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '唯一编码',
                               `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                               `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='权限控制-页面访问表';

-- ----------------------------
-- Table structure for t_auth_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_auth_role` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色名称',
                               `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色唯一编码',
                               `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                               `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='权限控制--角色表';

-- ----------------------------
-- Table structure for t_auth_role_element_operation
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_auth_role_element_operation` (
                                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                                 `roleId` bigint DEFAULT NULL COMMENT '角色id',
                                                 `elementOperationId` bigint DEFAULT NULL COMMENT '元素操作id',
                                                 `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='权限控制--角色与元素操作关联表';

-- ----------------------------
-- Table structure for t_auth_role_menu
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_auth_role_menu` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                    `roleId` bigint DEFAULT NULL COMMENT '角色id',
                                    `menuId` bigint DEFAULT NULL COMMENT '页面菜单id',
                                    `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='权限控制--角色页面菜单关联表';

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_user_role` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `userId` bigint DEFAULT NULL COMMENT '用户id',
                               `roleId` bigint DEFAULT NULL COMMENT '角色id',
                               `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                               PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色关联表';

-- ----------------------------
-- Table structure for t_refresh_token
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_refresh_token` (
                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                   `userId` bigint DEFAULT NULL COMMENT '用户id',
                                   `refreshToken` varchar(500) DEFAULT NULL COMMENT '刷新令牌',
                                   `createTime` datetime DEFAULT NULL COMMENT '创建时间',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='刷新令牌记录表';

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_file`  (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                           `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件存储路径',
                           `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型',
                           `md5` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件md5唯一标识串',
                           `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_video
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_video`  (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                            `userId` bigint NOT NULL COMMENT '用户id',
                            `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频链接',
                            `thumbnail` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封面链接',
                            `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频标题',
                            `type` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频类型：0原创 1转载',
                            `duration` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '视频时长',
                            `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所在分区：0鬼畜 1音乐 2电影',
                            `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '视频简介',
                            `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
                            `updateTime` datetime NULL DEFAULT NULL COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '视频投稿记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_tag`  (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                          `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签名称',
                          `createTime` datetime NULL DEFAULT NULL,
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_video_tag
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_video_tag`  (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                `videoId` bigint NOT NULL COMMENT '视频id',
                                `tagId` bigint NOT NULL COMMENT '标签id',
                                `createTime` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '视频标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_video_like
-- ----------------------------
CREATE TABLE IF NOT EXISTS `t_video_like`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
                                 `userId` bigint NOT NULL COMMENT '用户id',
                                 `videoId` bigint NOT NULL COMMENT '视频投稿id',
                                 `createTime` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '视频点赞记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
