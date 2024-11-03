CREATE TABLE store_manager.`user` (
  id varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '계정 아이디',
  password varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '비밀번호',
  name varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '사용자 이름',
  resident_regist_no CHAR(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '주민 번호',
  phone_no char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '전화번호',
  email varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '이메일',
  auth_cd CHAR(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '권한 코드',
  work_start_date DATE DEFAULT CURRENT_DATE NOT NULL COMMENT '근무 시작일',
  work_end_date DATE NULL COMMENT '근무 종료일',
  bank_name varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '급여 계좌 은행 이름',
  bank_account_no INT UNSIGNED NULL COMMENT '급여 계좌 번호',
  month_salary INT UNSIGNED NULL COMMENT '월급',
  hour_wage INT UNSIGNED NULL COMMENT '시급',
  created_by varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '생성자',
  created_date DATETIME NOT NULL COMMENT '생성일시',
  last_updated_by varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '마지막으로 업데이트한 사용자',
  last_updated_date DATETIME NOT NULL COMMENT '마지막으로 업데이트된 일시',
  CONSTRAINT user_pk PRIMARY KEY (id),
  CONSTRAINT user_unique_resident_no UNIQUE KEY (resident_regist_no),
  CONSTRAINT user_unique_phone UNIQUE KEY (phone_no),
  CONSTRAINT user_unique_email UNIQUE KEY (email)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_bin;
