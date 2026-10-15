-- ============================================================
-- 实验一 数据库初始化脚本：以「学生选课体系」为业务载体
-- 数据库：ssm_student
-- 四张表：class 班级、student 学生、course 课程、stu_course 选课
-- 关系：student.class_id -> class（多对一）
--       student <-> course 通过 stu_course（多对多）
-- ============================================================
DROP DATABASE IF EXISTS ssm_student;
CREATE DATABASE ssm_student DEFAULT CHARACTER SET utf8mb4;
USE ssm_student;

-- 班级信息表
CREATE TABLE class (
    class_id     INT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
    class_name   VARCHAR(30) NOT NULL COMMENT '班级名称',
    teacher_name VARCHAR(50) COMMENT '班主任',
    class_room   VARCHAR(30) COMMENT '教室'
) COMMENT='班级信息表';

INSERT INTO class (class_name, teacher_name, class_room) VALUES
('软工2401','张老师','教A201'),
('计科2401','李老师','教B302'),
('AI2401','王老师','教C405'),
('数科2401','赵老师','教D107');

-- 学生信息表
CREATE TABLE student (
    student_id  INT PRIMARY KEY AUTO_INCREMENT COMMENT '学生主键ID',
    student_no  VARCHAR(20) NOT NULL COMMENT '学号',
    name        VARCHAR(50) NOT NULL COMMENT '姓名',
    gender      CHAR(1) DEFAULT '男' COMMENT '性别',
    major       VARCHAR(50) COMMENT '专业',
    stu_class   VARCHAR(30) COMMENT '班级',
    class_id    INT COMMENT '所属班级ID（外键关联 class 表）',
    age         INT COMMENT '年龄',
    score       DECIMAL(5,2) COMMENT '期末成绩',
    enroll_date DATE COMMENT '入学时间',
    status      TINYINT DEFAULT 1 COMMENT '状态:1在读 0休学',
    CONSTRAINT fk_student_class FOREIGN KEY (class_id) REFERENCES class(class_id)
) COMMENT='学生信息表';

INSERT INTO student (student_no, name, gender, major, stu_class, class_id, age, score, enroll_date, status) VALUES
('2024010201','张小明','男','软件工程','软工2401',1,20,88.50,'2024-09-01',1),
('2024010202','李思思','女','计算机科学','计科2401',2,19,92.00,'2024-09-01',1),
('2024010203','王大力','男','软件工程','软工2401',1,21,76.30,'2023-09-01',1),
('2024010204','赵小雪','女','人工智能','AI2401',3,20,68.00,'2024-09-01',0),
('2024010205','陈浩然','男','数据科学','数科2401',4,19,85.00,'2024-09-01',1);

-- 课程信息表
CREATE TABLE course (
    course_id     INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
    course_name   VARCHAR(50) NOT NULL COMMENT '课程名称',
    credit        DECIMAL(3,1) COMMENT '学分',
    course_teacher VARCHAR(50) COMMENT '授课教师'
) COMMENT='课程信息表';

INSERT INTO course (course_name, credit, course_teacher) VALUES
('Java程序设计',3.5,'张老师'),
('数据库原理',3.0,'李老师'),
('数据结构',4.0,'王老师'),
('操作系统',3.5,'赵老师'),
('软件工程导论',2.0,'刘老师');

-- 选课表（学生与课程的多对多关联）
CREATE TABLE stu_course (
    sc_id        INT PRIMARY KEY AUTO_INCREMENT COMMENT '选课ID',
    student_id   INT NOT NULL COMMENT '学生ID（外键 student）',
    course_id    INT NOT NULL COMMENT '课程ID（外键 course）',
    course_score DECIMAL(5,2) COMMENT '该门课程成绩',
    CONSTRAINT fk_sc_student FOREIGN KEY (student_id) REFERENCES student(student_id),
    CONSTRAINT fk_sc_course  FOREIGN KEY (course_id) REFERENCES course(course_id)
) COMMENT='选课信息表';

INSERT INTO stu_course (student_id, course_id, course_score) VALUES
(1,1,92.00),(1,2,85.00),(1,3,88.00),
(2,2,95.00),(2,4,79.00),
(3,1,76.00),(3,5,82.00),
(4,1,68.00),
(5,3,90.00),(5,5,87.00);

-- 为姓名列建立普通索引，优化查询
CREATE INDEX idx_student_name ON student(name);
CREATE INDEX idx_sc_student ON stu_course(student_id);
CREATE INDEX idx_sc_course  ON stu_course(course_id);