package com.lab.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.student.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生持久层接口。
 * - 任务 2/3：手写自定义方法 + XML 动态 SQL（见 StudentMapper.xml）；
 * - 任务 4：继承 BaseMapper 获得零 SQL 通用 CRUD 与分页（XML 方法与通用方法共存）。
 */
public interface StudentMapper extends BaseMapper<Student> {

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

    /** 多表联合查询：查询学生及其所属班级信息（student JOIN class，回填班主任/教室） */
    List<Student> findWithClass();

    /** 多表联合查询 + 动态条件：按姓名/专业/班级名称筛选（LEFT JOIN + where 动态 SQL） */
    List<Student> findWithClassByCondition(Student student);

    /** 高级联查：学生带班级信息与所选课程列表（student+class+stu_course+course，一对多 collection） */
    List<Student> findStudentWithCourses();

    /** 统计：每门课程的选课人数（多对多分组统计） */
    java.util.List<java.util.Map<String, Object>> countCourseEnrollment();
}