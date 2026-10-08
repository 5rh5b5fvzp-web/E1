package com.lab.student.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * MyBatis 原生工具类（任务 2/3 使用）。
 * 使用原生 SqlSessionFactoryBuilder 构建会话工厂，用于纯 XML 映射编程。
 */
public final class MybatisUtil {

    /** 全局唯一会话工厂（提升 / 应用启动时只构建一次） */
    private static final SqlSessionFactory FACTORY = buildFactory();

    private MybatisUtil() {}

    private static SqlSessionFactory buildFactory() {
        try (InputStream in = Resources.getResourceAsStream("mybatis-config.xml")) {
            return new SqlSessionFactoryBuilder().build(in);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("构建 SqlSessionFactory 失败: " + e.getMessage());
        }
    }

    /**
     * 开启一个会话。
     * @param autoCommit 是否自动提交事务（增删改场景建议手动 commit，故默认 false）
     */
    public static SqlSession openSession() {
        return FACTORY.openSession(false); // 手动提交
    }

    /** 获取全局唯一的会话工厂（便于演示） */
    public static SqlSessionFactory getFactory() {
        return FACTORY;
    }
}