package com.hack.auroranite.Models;

public class Course {
    private String courseName;
    private String credit;
    private String link;

    public Course() {
    }

    public Course(String courseName, String credit, String link) {
        this.courseName = courseName;
        this.credit = credit;
        this.link = link;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
