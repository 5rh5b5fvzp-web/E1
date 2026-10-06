-- ============================================================
-- 实验一 数据库初始化脚本：以「学生表」为业务载体
-- 数据库：ssm_student
-- ============================================================
DROP DATABASE IF EXISTS ssm_student;
CREATE DATABASE ssm_student DEFAULT CHARACTER SET utf8mb4;
USE ssm_student;

CREATE TABLE student (
    student_id  INT PRIMARY KEY AUTO_INCREMENT COMMENT '学生主键ID',
    student_no  VARCHAR(20)   NOT NULL COMMENT '学号',
    name        VARCHAR(50)   NOT NULL COMMENT '姓名',
    gender      CHAR(1)       DEFAULT '男' COMMENT '性别',
    major       VARCHAR(50)   COMMENT '专业',
    stu_class   VARCHAR(30)   COMMENT '班级',
    age         INT           COMMENT '年龄',
    score       DECIMAL(5,2)  COMMENT '期末成绩(0-100分)',
    enroll_date DATE          COMMENT '入学时间',
    status      TINYINT       DEFAULT 1 COMMENT '状态:1在读 0休学'
) COMMENT='学生信息表';

INSERT INTO student (student_no, name, gender, major, stu_class, age, score, enroll_date, status) VALUES
('2024010201','张小明','男','软件工程','软工2401',20,88.50,'2024-09-01',1),
('2024010202','李思思','女','计算机科学','计科2401',19,92.00,'2024-09-01',1),
('2024010203','王大力','男','软件工程','软工2401',21,76.30,'2023-09-01',1),
('2024010204','赵小雪','女','人工智能','AI2401',20,68.00,'2024-09-01',0),
('2024010205','陈浩然','男','数据科学','数科2401',19,85.00,'2024-09-01',1);

-- 为姓名列建立普通索引，优化查询
CREATE INDEX idx_student_name ON student(name);