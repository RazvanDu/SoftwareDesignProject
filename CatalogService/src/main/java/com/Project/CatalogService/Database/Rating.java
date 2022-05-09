package com.Project.CatalogService.Database;

import javax.persistence.*;

@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    int id;

    @Column(name = "Name", length = 100, nullable = false)
    String name;

    @Column(name = "Age", nullable = false)
    int age;

    @Column(name = "Type", length = 3)
    String type;

    public Rating() {
    }

    public Rating( String name, int age, String type) {

        this.name = name;
        this.age = age;
        this.type = type;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
