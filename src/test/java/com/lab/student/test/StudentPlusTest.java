package com.lab.student.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.student.entity.Student;
import com.lab.student.mapper.StudentMapper;
import com.lab.student.util.MybatisPlusUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * 任务 4：MyBatis-Plus 通用 CRUD / 条件构造器 / 分页插件 单元测试。
 * 使用 MP 的 MybatisPlusUtil 构建会话，分页插件已通过 Java API 注册。
 */
public class StudentPlusTest {

    @Test
    public void testSelectById() {
        try (SqlSession session = MybatisPlusUtil.openSession()) {
            Student s = session.getMapper(StudentMapper.class).selectById(1);
            System.out.println(s);
            Assert.assertNotNull(s);
            Assert.assertEquals("张小明", s.getName());
        }
    }

    @Test
    public void testSelectListByWrapper() {
        try (SqlSession session = MybatisPlusUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            LambdaQueryWrapper<Student> wrap = new LambdaQueryWrapper<>();
            wrap.like(Student::getName, "小")
                    .eq(Student::getStatus, 1)
                    .ge(Student::getScore, 80)
                    .orderByDesc(Student::getScore);
            List<Student> list = mapper.selectList(wrap);
            list.forEach(System.out::println);
            Assert.assertTrue(list.size() > 0);
        }
    }

    @Test
    public void testPage() {
        try (SqlSession session = MybatisPlusUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            Page<Student> page = new Page<>(1, 3);
            Page<Student> result = mapper.selectPage(page, null);
            System.out.println("总记录数 = " + result.getTotal());
            System.out.println("总页数 = " + result.getPages());
            result.getRecords().forEach(System.out::println);
            Assert.assertEquals(5L, result.getTotal());
            Assert.assertEquals(3, result.getRecords().size()); // 每页 3 条 -> 页大小为 3
        }
    }

    @Test
    public void testInsertAndDelete() {
        try (SqlSession session = MybatisPlusUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            Student s = new Student();
            s.setStudentNo("2024010401");
            s.setName("MP测试");
            s.setGender("女");
            s.setMajor("软件工程");
            s.setStuClass("软工2401");
            s.setAge(20);
            s.setScore(new BigDecimal("95.50"));
            s.setStatus(1);
            // BaseMapper.insert：零 SQL
            int rows = mapper.insert(s);
            Assert.assertEquals(1, rows);
            System.out.println("MP 新增成功，回填主键 = " + s.getStudentId());

            // 清理
            mapper.deleteById(s.getStudentId());
            System.out.println("MP 删除成功");
        }
    }
}