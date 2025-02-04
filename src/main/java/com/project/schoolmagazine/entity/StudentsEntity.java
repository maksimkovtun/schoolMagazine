package com.project.schoolmagazine.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "students", schema = "public", catalog = "schoolMagazine")
public class StudentsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "student_id")
    private Integer studentId;
    @Basic
    @Column(name = "student_name")
    private String studentName;
    @Basic
    @Column(name = "student_surname")
    private String studentSurname;
    @Basic
    @Column(name = "student_patronymic")
    private String studentPatronymic;
    @Basic
    @Column(name = "class_number")
    private Integer classNumber;
    @Basic
    @Column(name = "class_letter")
    private String classLetter;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

    public String getStudentPatronymic() {
        return studentPatronymic;
    }

    public void setStudentPatronymic(String studentPatronymic) {
        this.studentPatronymic = studentPatronymic;
    }

    public Integer getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(Integer classNumber) {
        this.classNumber = classNumber;
    }

    public String getClassLetter() {
        return classLetter;
    }

    public void setClassLetter(String classLetter) {
        this.classLetter = classLetter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentsEntity that = (StudentsEntity) o;
        return Objects.equals(studentId, that.studentId) && Objects.equals(studentName, that.studentName) && Objects.equals(studentSurname, that.studentSurname) && Objects.equals(studentPatronymic, that.studentPatronymic) && Objects.equals(classNumber, that.classNumber) && Objects.equals(classLetter, that.classLetter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, studentName, studentSurname, studentPatronymic, classNumber, classLetter);
    }

    public StudentsEntity() {}

    public StudentsEntity(String studentName, String studentSurname, String studentPatronymic, Integer classNumber, String classLetter) {
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.studentPatronymic = studentPatronymic;
        this.classNumber = classNumber;
        this.classLetter = classLetter;
    }

    @Override
    public String toString() {
        return "StudentsEntity{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", studentSurname='" + studentSurname + '\'' +
                ", studentPatronymic='" + studentPatronymic + '\'' +
                ", classNumber=" + classNumber +
                ", classLetter='" + classLetter + '\'' +
                '}';
    }
}
