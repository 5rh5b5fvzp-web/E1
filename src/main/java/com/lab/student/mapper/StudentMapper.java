package com.lab.student.mapper;

import com.lab.student.entity.Student;

import java.util.List;

/**
 * 学生持久层接口（任务 2：基础 CRUD）。
 * 任务 4 阶段将继承 BaseMapper 获得 MyBatis-Plus 通用能力。
 */
public interface StudentMapper {

    /** 查询全部学生 */
    List<Student> findAll();

    /** 按主键查询单个学生 */
    Student findByPk(Integer studentId);

    /** 新增学生并回填自增主键 */
    int addStudent(Student student);

    /** 按主键全字段更新 */
    int modifyStudent(Student student);

    /** 按主键删除单个学生 */
    int removeByPk(Integer studentId);
}