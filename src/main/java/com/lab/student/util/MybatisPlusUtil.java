package com.lab.student.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.baomidou.mybatisplus.core.MybatisXMLConfigBuilder;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.InputStream;

/**
 * MyBatis-Plus 工具类（任务 4 使用）。
 * 要点：
 * 1) 必须用 MP 提供的 MybatisSqlSessionFactoryBuilder.build(Configuration)，
 *    否则 BaseMapper 方法无法注册，会报 Invalid bound statement 异常；
 * 2) 分页插件必须通过 Java API 注册（XML 中写 @PaginationInnerInterceptor 会解析报错）。
 */
public final class MybatisPlusUtil {

    /** 全局唯一会话工厂 */
    private static final SqlSessionFactory FACTORY = buildFactory();

    private MybatisPlusUtil() {}

    private static SqlSessionFactory buildFactory() {
        try (InputStream in = Resources.getResourceAsStream("mybatis-config.xml")) {
            // 1. 用 MP 的 XML 配置构建器解析配置，复用数据源/别名/映射器扫描等
            Configuration configuration = new MybatisXMLConfigBuilder(in).parse();

            // 2. 通过 Java API 注册分页插件（XML 写法会解析报错，必须走代码注册）
            MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
            configuration.addInterceptor(interceptor);

            // 3. 用 MP 的会话工厂构建器构建（BaseMapper 方法可用的关键）
            return new MybatisSqlSessionFactoryBuilder().build(configuration);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("构建 MyBatis-Plus SqlSessionFactory 失败: " + e.getMessage());
        }
    }

    /** 开启一个自动提交事务的会话（MP 演示增删改时省去手动 commit） */
    public static SqlSession openSession() {
        return FACTORY.openSession(true);
    }
}