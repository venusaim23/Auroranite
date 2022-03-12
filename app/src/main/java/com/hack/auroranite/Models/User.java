package com.hack.auroranite.Models;

public class User {
    private String UID;
    private String name;
    private String email;
    private String education;
    private String major;
    private String gradYear;
    private String country;
    private String skills;
    private String experience;
    private String salary;
    private String workMode;

    public User() {
    }

    public User(String UID, String name, String email, String education, String major, String gradYear,
                String country, String skills, String experience, String salary, String workMode) {
        this.UID = UID;
        this.name = name;
        this.email = email;
        this.education = education;
        this.major = major;
        this.gradYear = gradYear;
        this.country = country;
        this.skills = skills;
        this.experience = experience;
        this.salary = salary;
        this.workMode = workMode;
    }

    public String getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGradYear() {
        return gradYear;
    }

    public void setGradYear(String gradYear) {
        this.gradYear = gradYear;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getWorkMode() {
        return workMode;
    }

    public void setWorkMode(String workMode) {
        this.workMode = workMode;
    }
}
