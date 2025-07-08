package com.project.schoolmagazine.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "profiles", schema = "public", catalog = "schoolMagazine")
public class ProfilesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Integer profileId;
    @Basic
    @Column(name = "profile_name")
    private String profileName;
    @Basic
    @Column(name = "profile_surname")
    private String profileSurname;
    @Basic
    @Column(name = "profile_patronymic")
    private String profilePatronymic;
    @Basic
    @Column(name = "class_number")
    private Integer classNumber;
    @Basic
    @Column(name = "class_letter")
    private String classLetter;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;
    @Basic
    @Column(name = "profile_mail")
    private String profileMail;
    public Integer getProfileId() {
        return profileId;
    }
    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }
    public String getProfileName() {
        return profileName;
    }
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
    public String getProfileSurname() {
        return profileSurname;
    }
    public void setProfileSurname(String profileSurname) {
        this.profileSurname = profileSurname;
    }
    public void setProfileMail(String profileMail) {
        this.profileMail = profileMail;
    }
    public String getProfilePatronymic() {
        return profilePatronymic;
    }
    public void setProfilePatronymic(String profilePatronymic) {
        this.profilePatronymic = profilePatronymic;
    }
    public void setClassLetter(String classLetter) {
        this.classLetter = classLetter;
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
    public String getProfileMail() {
        return profileMail;
    }
    public UsersEntity getUser() {
        return user;
    }
    public void setUser(UsersEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfilesEntity that = (ProfilesEntity) o;
        return Objects.equals(profileId, that.profileId) && Objects.equals(profileName, that.profileName) && Objects.equals(profileSurname, that.profileSurname) && Objects.equals(profilePatronymic, that.profilePatronymic) && Objects.equals(classNumber, that.classNumber) && Objects.equals(classLetter, that.classLetter);
    }
    @Override
    public int hashCode() {
        return Objects.hash(profileId, profileName, profileSurname, profilePatronymic, classNumber, classLetter);
    }

    public ProfilesEntity() {}

    public void updateFields(String surname, String name, String patronymic, String mail, Integer classNumber, String classLetter) {
        this.profileSurname = surname != null ? surname.trim() : null;
        this.profileName = name != null ? name.trim() : null;
        this.profilePatronymic = patronymic != null ? patronymic.trim() : null;
        this.profileMail = mail != null ? mail.trim() : null;
        this.classNumber = classNumber;
        this.classLetter = classLetter != null ? classLetter.trim().toUpperCase() : null;
    }

    @Override
    public String toString() {
        return "profilesEntity{" +
                "profileId=" + profileId +
                ", profileName='" + profileName + '\'' +
                ", profileSurname='" + profileSurname + '\'' +
                ", profilePatronymic='" + profilePatronymic + '\'' +
                ", classNumber=" + classNumber +
                ", classLetter='" + classLetter + '\'' +
                ", profileMail='" + profileMail + '\'' +
                '}';
    }
}
