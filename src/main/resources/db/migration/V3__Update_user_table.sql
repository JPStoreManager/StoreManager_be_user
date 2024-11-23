ALTER TABLE store_manager.`user` CHANGE work_status work_status_cd char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '근무 상태 (재직, 퇴사, ..)';
ALTER TABLE store_manager.`user` MODIFY COLUMN bank_account_no varchar(20) DEFAULT NULL NULL COMMENT '급여 계좌 번호';
