package com.lab.student.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生实体类（业务载体：学生表 student）
 * 同时被 MyBatis XML 映射与 MyBatis-Plus 通用 Mapper 使用：
 * 手写 XML 时靠 resultMap 完成字段映射；
 * MyBatis-Plus 时靠 @TableName/@TableId/@TableField 注解完成映射。
 */
@TableName("student")
public class Student {

    /** 学生主键ID（自增） */
    @TableId(value = "student_id", type = IdType.AUTO)
    private Integer studentId;

    /** 学号 */
    @TableField("student_no")
    private String studentNo;

    /** 姓名 */
    private String name;

    /** 性别 */
    private String gender;

    /** 专业 */
    private String major;

    /** 班级 */
    @TableField("stu_class")
    private String stuClass;

    /** 年龄 */
    private Integer age;

    /** 期末成绩 */
    private BigDecimal score;

    /** 入学时间 */
    private Date enrollDate;

    /** 状态：1在读 0休学 */
    private Integer status;

    public Integer getStudentId() { return studentId; }
    public void setStudentId(Integer studentId) { this.studentId = studentId; }

    public String getStudentNo() { return studentNo; }
    public void setStudentNo(String studentNo) { this.studentNo = studentNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getStuClass() { return stuClass; }
    public void setStuClass(String stuClass) { this.stuClass = stuClass; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }

    public Date getEnrollDate() { return enrollDate; }
    public void setEnrollDate(Date enrollDate) { this.enrollDate = enrollDate; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    @Override
    public String toString() {
        return "Student{studentId=" + studentId
                + ", studentNo='" + studentNo + '\''
                + ", name='" + name + '\''
                + ", gender='" + gender + '\''
                + ", major='" + major + '\''
                + ", stuClass='" + stuClass + '\''
                + ", age=" + age
                + ", score=" + score
                + ", enrollDate=" + enrollDate
                + ", status=" + status + '}';
    }
}