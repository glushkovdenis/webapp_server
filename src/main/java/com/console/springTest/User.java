package com.console.springTest;

public class User {
    private Integer id = null;
    private String name = "";
    private String surname = "";
    private String patronymic = "";
    private String birth = "";

    public User() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getPatronymic() {
        return this.patronymic;
    }

    public String getBirth() {
        return this.birth;
    }
}
