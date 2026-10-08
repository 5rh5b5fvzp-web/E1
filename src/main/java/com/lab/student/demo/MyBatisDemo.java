package com.lab.student.demo;

import com.lab.student.entity.Student;
import com.lab.student.mapper.StudentMapper;
import com.lab.student.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 任务 2.4：第一个 MyBatis 程序（原生 XML 映射）。
 * 演示 MyBatis 执行流程：读配置 -> 构建 SqlSessionFactory -> 开启 SqlSession
 *                              -> 获取 Mapper 代理 -> 执行 SQL -> 结果映射 -> 返回对象。
 */
public class MyBatisDemo {

    public static void main(String[] args) {
        try (SqlSession session = MybatisUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);

            System.out.println("====== 查询全部学生 ======");
            List<Student> list = mapper.findAll();
            list.forEach(System.out::println);
        }
    }
}