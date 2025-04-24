-- 스키마가 없는 경우 생성
CREATE SCHEMA IF NOT EXISTS user_data;

-- 테이블이 없는 경우 생성
CREATE TABLE IF NOT EXISTS user_data.user_info (
    user_info_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id VARCHAR(255) NOT NULL UNIQUE,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_data.user_auth (
    user_auth_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_info_id UUID NOT NULL REFERENCES user_data.user_info(user_info_id),
    email VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_by VARCHAR(255),
    created_at TIMESTAMP,
    updated_by VARCHAR(255),
    updated_at TIMESTAMP
);
/* 
-- 테이블이 있다면 데이터 삭제 (외래 키를 가진 테이블부터 삭제)
TRUNCATE TABLE user_data.user_auth;
TRUNCATE TABLE user_data.user_info;  */