package com.lab.student.demo;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.student.entity.Student;
import com.lab.student.mapper.StudentMapper;
import com.lab.student.util.MybatisPlusUtil;
import org.apache.ibatis.session.SqlSession;

/**
 * 任务 4：MyBatis-Plus 快速开发演示。
 * 1) BaseMapper 零 SQL 通用 CRUD；
 * 2) 条件构造器（LambdaQueryWrapper）实现多条件模糊 + 范围 + 分页 + 排序；
 * 3) 分页插件查询。
 */
public class MyBatisPlusDemo {

    public static void main(String[] args) {
        try (SqlSession session = MybatisPlusUtil.openSession()) {
            StudentMapper mapper = session.getMapper(StudentMapper.class);

            // 1. BaseMapper 内置方法：按主键查询
            System.out.println("====== BaseMapper.selectById(1) ======");
            System.out.println(mapper.selectById(1));

            // 2. 条件构造器：多条件模糊 + 范围 + 排序
            System.out.println("====== LambdaQueryWrapper 多条件查询 ======");
            LambdaQueryWrapper<Student> wrap = new LambdaQueryWrapper<>();
            wrap.like(Student::getName, "小")
                    .eq(Student::getStatus, 1)
                    .ge(Student::getScore, 80)
                    .orderByDesc(Student::getScore);
            mapper.selectList(wrap).forEach(System.out::println);

            // 3. 分页查询：第 1 页，每页 3 条
            System.out.println("====== 分页查询：第 1 页，每页 3 条 ======");
            Page<Student> page = new Page<>(1, 3);
            Page<Student> result = mapper.selectPage(page, null);
            System.out.println("总记录数 = " + result.getTotal());
            System.out.println("总页数 = " + result.getPages());
            result.getRecords().forEach(System.out::println);
        }
    }
}