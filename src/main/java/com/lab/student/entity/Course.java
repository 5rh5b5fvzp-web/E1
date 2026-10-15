package com.lab.student.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

/**
 * 课程实体类（业务载体：课程表 course）。
 * 通过选课表 stu_course 与学生表形成多对多关系。
 */
@TableName("course")
public class Course {

    /** 课程ID（自增） */
    @TableId(value = "course_id", type = IdType.AUTO)
    private Integer courseId;

    /** 课程名称 */
    private String courseName;

    /** 学分 */
    private BigDecimal credit;

    /** 授课教师 */
    private String courseTeacher;

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public BigDecimal getCredit() { return credit; }
    public void setCredit(BigDecimal credit) { this.credit = credit; }

    public String getCourseTeacher() { return courseTeacher; }
    public void setCourseTeacher(String courseTeacher) { this.courseTeacher = courseTeacher; }

    @Override
    public String toString() {
        return "Course{courseId=" + courseId
                + ", courseName='" + courseName + '\''
                + ", credit=" + credit
                + ", courseTeacher='" + courseTeacher + '\'' + '}';
    }
}