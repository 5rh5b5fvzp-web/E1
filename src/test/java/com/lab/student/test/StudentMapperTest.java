package com.lab.student.test;

import com.lab.student.entity.Student;
import com.lab.student.mapper.StudentMapper;
import com.lab.student.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 任务 2 的 JUnit 单元测试（原生 MyBatis + 基础 CRUD）。
 * 注意：增删改均手动 session.commit() 提交事务；
 * 测试会在末尾删除新增/修改的数据，保证可重复执行而不污染数据。
 */
public class StudentMapperTest {

    @Test
    public void testFindAll() {
        try (SqlSession session = MybatisUtil.openSession()) {
            List<Student> list = session.getMapper(StudentMapper.class).findAll();
            list.forEach(System.out::println);
            Assert.assertEquals(5, list.size());
        }
    }

    @Test
    public void testFindByPk() {
        try (SqlSession session = MybatisUtil.openSession()) {
            Student s = session.getMapper(StudentMapper.class).findByPk(1);
            System.out.println(s);
            Assert.assertNotNull(s);
            Assert.assertEquals("张小明", s.getName());
        }
    }

    @Test
    public void testAddAndDelete() {
        try (SqlSession session = MybatisUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            Student s = new Student();
            s.setStudentNo("2024010299");
            s.setName("测试同学");
            s.setGender("男");
            s.setMajor("软件工程");
            s.setStuClass("软工2401");
            s.setAge(20);
            s.setScore(new BigDecimal("90.00"));
            s.setEnrollDate(new Date());
            s.setStatus(1);

            int rows = mapper.addStudent(s);   // useGeneratedKeys 回填主键
            session.commit();
            Assert.assertEquals(1, rows);
            System.out.println("回填主键 studentId = " + s.getStudentId());
            Assert.assertNotNull("自增主键应已回填", s.getStudentId());

            // 清理临时数据，保证可重复执行
            mapper.removeByPk(s.getStudentId());
            session.commit();
        }
    }

    @Test
    public void testUpdate() {
        try (SqlSession session = MybatisUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            Student s = mapper.findByPk(1);
            BigDecimal oldScore = s.getScore();
            s.setScore(new BigDecimal("99.00"));
            int rows = mapper.modifyStudent(s);
            session.commit();
            Assert.assertEquals(1, rows);
            // 还原数据
            s.setScore(oldScore);
            mapper.modifyStudent(s);
            session.commit();
        }
    }
}