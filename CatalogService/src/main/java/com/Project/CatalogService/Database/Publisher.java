package com.Project.CatalogService.Database;

import javax.persistence.*;

@Entity
@Table(name = "publishers")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    int id;

    @Column(name = "Name", length = 100, nullable = false, unique = true)
    String name;

    @Column(name = "Type", length = 100)
    String type;

    @Column(name = "Logo", length = 512)
    String logo;

    @Column(name = "Website", length = 512)
    String website;

    public Publisher() {
    }

    public Publisher(String name, String type, String logo, String website) {

        this.name = name;
        this.type = type;
        this.logo = logo;
        this.website = website;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
