package com.lab.student.test;

import com.lab.student.entity.Student;
import com.lab.student.mapper.StudentMapper;
import com.lab.student.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 任务 2/3 的 JUnit 单元测试（原生 MyBatis + XML 动态 SQL）。
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

    // ---- 任务 3：动态 SQL ----

    @Test
    public void testQueryByCondition() {
        try (SqlSession session = MybatisUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            Student cond = new Student();
            cond.setName("小");      // 模糊查询
            cond.setStatus(1);       // 状态
            List<Student> list = mapper.queryByCondition(cond);
            list.forEach(System.out::println);
            Assert.assertTrue(list.size() > 0);
        }
    }

    @Test
    public void testModifyDynamic() {
        try (SqlSession session = MybatisUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            // 只传入非空字段 -> 动态更新（set 标签自动处理）
            Student patch = new Student();
            patch.setStudentId(5);
            patch.setAge(20);
            int rows = mapper.modifyDynamic(patch);
            session.commit();
            Assert.assertEquals(1, rows);
            // 还原年龄
            Student patch2 = new Student();
            patch2.setStudentId(5);
            patch2.setAge(19);
            mapper.modifyDynamic(patch2);
            session.commit();
        }
    }

    @Test
    public void testAddBatchAndRemoveBatch() {
        try (SqlSession session = MybatisUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);

            Student s1 = new Student();
            s1.setStudentNo("2024010301"); s1.setName("批量甲"); s1.setGender("男");
            s1.setMajor("软件工程"); s1.setStatus(1);
            Student s2 = new Student();
            s2.setStudentNo("2024010302"); s2.setName("批量乙"); s2.setGender("女");
            s2.setMajor("人工智能"); s2.setStatus(1);
            int rows = mapper.addBatch(Arrays.asList(s1, s2));
            session.commit();
            Assert.assertEquals(2, rows);
            System.out.println("批量插入成功：" + rows + " 条");

            // 根据学号定位并删除这 2 条（可重复执行）
            List<Student> created = mapper.findAll();
            Integer id1 = null, id2 = null;
            for (Student st : created) {
                if ("2024010301".equals(st.getStudentNo())) id1 = st.getStudentId();
                if ("2024010302".equals(st.getStudentNo())) id2 = st.getStudentId();
            }
            int del = mapper.removeBatch(Arrays.asList(id1, id2));
            session.commit();
            Assert.assertEquals(2, del);
            System.out.println("批量删除成功：" + del + " 条");
        }
    }

    @Test
    public void testQueryByPriority() {
        try (SqlSession session = MybatisUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            // 姓名优先：命中精确姓名的学生
            List<Student> byName = mapper.queryByPriority("张小明", null);
            System.out.println("按姓名精确查询：" + byName.size() + " 条");
            Assert.assertEquals(1, byName.size());
            Assert.assertEquals("张小明", byName.get(0).getName());

            // 姓名为空 -> 按专业查询
            List<Student> byMajor = mapper.queryByPriority(null, "人工智能");
            System.out.println("按专业查询：" + byMajor.size() + " 条");
            Assert.assertTrue(byMajor.size() > 0);
        }
    }

    // ---- 多表联合查询（student JOIN class）----

    @Test
    public void testFindWithClass() {
        try (SqlSession session = MybatisUtil.openSession()) {
            List<Student> list = session.getMapper(StudentMapper.class).findWithClass();
            list.forEach(System.out::println);
            Assert.assertTrue(list.size() >= 5);
            // 张小明（学号 2024010201）应回填其班级的班主任与教室
            Student zhang = list.stream()
                    .filter(s -> "2024010201".equals(s.getStudentNo()))
                    .findFirst().orElse(null);
            Assert.assertNotNull(zhang);
            Assert.assertEquals("张老师", zhang.getTeacherName());
            Assert.assertEquals("教A201", zhang.getClassRoom());
        }
    }

    @Test
    public void testFindWithClassByCondition() {
        try (SqlSession session = MybatisUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);
            Student cond = new Student();
            cond.setStuClass("软工2401");   // 按班级名称筛选
            List<Student> list = mapper.findWithClassByCondition(cond);
            list.forEach(System.out::println);
            // 软工2401 有张小明、王大力两名学生
            Assert.assertEquals(2, list.size());
            Assert.assertEquals("张老师", list.get(0).getTeacherName());
        }
    }

    // ---- 高级查询：一对多 collection 联查与多对多统计 ----

    @Test
    public void testFindStudentWithCourses() {
        try (SqlSession session = MybatisUtil.openSession()) {
            List<Student> list = session.getMapper(StudentMapper.class).findStudentWithCourses();
            list.forEach(System.out::println);
            Assert.assertTrue(list.size() >= 5);
            // 张小明（学号 2024010201）应带出 3 门课程
            Student zhang = list.stream()
                    .filter(s -> "2024010201".equals(s.getStudentNo()))
                    .findFirst().orElse(null);
            Assert.assertNotNull(zhang);
            Assert.assertNotNull(zhang.getCourseList());
            Assert.assertEquals(3, zhang.getCourseList().size());
            Assert.assertEquals("Java程序设计", zhang.getCourseList().get(0).getCourseName());
        }
    }

    @Test
    public void testCountCourseEnrollment() {
        try (SqlSession session = MybatisUtil.openSession()) {
            List<java.util.Map<String, Object>> rows =
                    session.getMapper(StudentMapper.class).countCourseEnrollment();
            rows.forEach(System.out::println);
            // 5 门课程都应返回统计行
            Assert.assertEquals(5, rows.size());
            // 第一门课 Java程序设计 应有 3 人选课（张小明、王大力、赵小雪）
            Object first = rows.get(0).values().iterator().next();
            Assert.assertNotNull(first);
        }
    }
}