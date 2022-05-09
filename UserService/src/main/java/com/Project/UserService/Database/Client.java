package com.Project.UserService.Database;

import com.google.gson.JsonObject;

import javax.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    int id;

    @Column(name = "First_Name", length = 100, nullable = false)
    String firstName;

    @Column(name = "Last_Name", length = 100, nullable = false)
    String lastName;

    @Column(name = "Age", nullable = false)
    int age;

    @Column(name = "Email", nullable = false, unique = true)
    String email;

    @Column(name = "Password", length = 500, nullable = false)
    String password;

    @Column(name = "Seller", nullable = false)
    boolean seller;

    @Column(name = "Admin_status", nullable = false)
    boolean adminStatus;

    public Client(String firstName,
                  String lastName,
                  int age,
                  String email,
                  String password,
                  boolean seller,
                  boolean adminStatus) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.seller = seller;
        this.adminStatus = adminStatus;

    }

    public Client() {

    }

    public Client(String email) {
        this.email = email;
    }

    public Client(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public Client(int id, String email, String pswd) {
        this.id = id;
        this.email = email;
        this.password = pswd;
    }

    public Client(boolean exists, String message) {
        if (!exists) {
            this.id = -1;
            this.firstName = message;
            this.lastName = "";
            this.age = -1;
            this.email = "";
            this.password = "";
            this.seller = false;
            this.adminStatus = false;
        }
        else new Client();
    }

    public Client(JsonObject jsonObject) {

        this.id = jsonObject.get("id").getAsInt();
        this.firstName = jsonObject.get("firstName").getAsString();
        this.lastName = jsonObject.get("lastName").getAsString();
        this.age = jsonObject.get("age").getAsInt();
        this.email = jsonObject.get("email").getAsString();
        this.password = jsonObject.get("password").getAsString();
        this.seller = jsonObject.get("seller").getAsBoolean();
        this.adminStatus = jsonObject.get("adminStatus").getAsBoolean();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSeller() {
        return seller;
    }

    public void setSeller(boolean seller) {
        this.seller = seller;
    }

    public boolean isAdmin() {
        return adminStatus;
    }

    public void setAdmin(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Client)) {
            return false;
        }
        Client client = (Client) obj;
        return client.getId() == this.id &&
                client.getFirstName().equals(this.firstName) &&
                client.getLastName().equals(this.lastName) &&
                client.getAge() == this.age &&
                client.getEmail().equals(this.email) &&
                client.seller == this.seller &&
                client.adminStatus == this.adminStatus;

    }
}
