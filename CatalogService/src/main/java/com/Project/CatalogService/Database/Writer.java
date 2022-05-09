package com.Project.CatalogService.Database;

import javax.persistence.*;

@Entity
@Table(name = "writers")
public class Writer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    int id;

    @Column(name = "Name", length = 100)
    String name;

    @Column(name = "Picture", length = 100)
    String picture;

    @Column(name = "born")
    int born;

    @Column(name = "deceased")
    int deceased;

    public Writer() {
    }

    public Writer(String name, String picture, int born, int deceased) {

        this.name = name;
        this.picture = picture;
        this.born = born;
        this.deceased = deceased;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getBorn() {
        return born;
    }

    public void setBorn(int born) {
        this.born = born;
    }

    public int getDeceased() {
        return deceased;
    }

    public void setDeceased(int deceased) {
        this.deceased = deceased;
    }

}
