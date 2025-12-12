package ru.top.dz_19http.models;

import ru.top.dz_19http.dto.TeacherDTO;

public class Teacher {
    private Integer id;
    private String firstName;
    private String lastName;
    private String subject;
    private Integer experience;
    private Double salary;
    private String email;
    private Boolean isAtive;
    private static int nextId = 1;

    public Teacher(String firstName, String lastName, String subject, Integer experience, Double salary, String email, Boolean isAtive) {
        this.id = ++nextId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subject = subject;
        this.experience = experience;
        this.salary = salary;
        this.email = email;
        this.isAtive = isAtive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAtive() {
        return isAtive;
    }

    public void setAtive(Boolean ative) {
        isAtive = ative;
    }

    public TeacherDTO convert() {
        return new TeacherDTO(this.firstName, this.lastName, this.subject, this.experience, this.salary, this.email, this.isAtive);
    }
}
