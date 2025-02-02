ALTER TABLE store_manager.`user` ADD otp_no char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '비밀번호 찾긱용 OTP 번호';
ALTER TABLE store_manager.`user` CHANGE otp_no otp_no char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '비밀번호 찾긱용 OTP 번호' AFTER hour_wage;
