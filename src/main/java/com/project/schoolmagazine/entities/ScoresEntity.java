package com.project.schoolmagazine.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "scores", schema = "public", catalog = "schoolMagazine")
public class ScoresEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "score_id", nullable = false)
    private Integer scoreId;
    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "user_id", nullable = false)
    private UsersEntity teacher;
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", nullable = false)
    private UsersEntity student;
    @ManyToOne
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", nullable = false)
    private SubjectsEntity subject;
    @Basic
    @Column(name = "quarter", nullable = false)
    private Integer quarter;
    @Basic
    @Column(name = "score_date", nullable = false)
    private Date scoreDate;
    @Basic
    @Column(name = "score_value", nullable = false)
    private Integer scoreValue;

    public Integer getScoreId() {
        return scoreId;
    }

    public void setScoreId(Integer scoreId) {
        this.scoreId = scoreId;
    }

    public UsersEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(UsersEntity teacher) {
        this.teacher = teacher;
    }

    public UsersEntity getStudent() {
        return student;
    }

    public void setStudent(UsersEntity student) {
        this.student = student;
    }

    public SubjectsEntity getSubject() {
        return subject;
    }

    public void setSubject(SubjectsEntity subject) {
        this.subject = subject;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Date getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(Date scoreDate) {
        this.scoreDate = scoreDate;
    }

    public Integer getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(Integer scoreValue) {
        this.scoreValue = scoreValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScoresEntity that = (ScoresEntity) o;
        return Objects.equals(scoreId, that.scoreId)
                && Objects.equals(teacher, that.teacher)
                && Objects.equals(student, that.student)
                && Objects.equals(subject, that.subject)
                && Objects.equals(quarter, that.quarter)
                && Objects.equals(scoreDate, that.scoreDate)
                && Objects.equals(scoreValue, that.scoreValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoreId, teacher, student, subject, quarter, scoreDate, scoreValue);
    }

}
