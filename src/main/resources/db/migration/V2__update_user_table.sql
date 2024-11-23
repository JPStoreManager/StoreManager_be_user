ALTER TABLE store_manager.`user` ADD address varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '주소';
ALTER TABLE store_manager.`user` CHANGE address address varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '주소' AFTER email;
ALTER TABLE store_manager.`user` ADD work_status char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '근무 상태 (재직, 퇴사, ..)';
ALTER TABLE store_manager.`user` CHANGE work_status work_status char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '근무 상태 (재직, 퇴사, ..)' AFTER work_end_date;
