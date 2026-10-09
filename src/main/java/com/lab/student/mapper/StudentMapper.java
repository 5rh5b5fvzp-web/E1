package com.lab.student.mapper;

import com.lab.student.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生持久层接口。
 * 任务 2：基础 CRUD；任务 3：新增动态 SQL、多条件查询与批量操作；
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

    /** 多条件动态模糊查询（if / where） */
    List<Student> queryByCondition(Student student);

    /** 动态更新，仅更新传入的非空字段（set） */
    int modifyDynamic(Student student);

    /** 批量删除（foreach） */
    int removeBatch(@Param("ids") List<Integer> ids);

    /** 批量插入（foreach） */
    int addBatch(@Param("list") List<Student> list);

    /** 分支选择：优先按姓名精确查，否则按专业查，否则查全部（choose/when/otherwise） */
    List<Student> queryByPriority(@Param("name") String name, @Param("major") String major);
}